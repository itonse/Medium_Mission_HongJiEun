package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.dto.WriteForm;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import com.ll.medium.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.ll.medium.global.exception.ErrorCode.NOT_FOUND_POST;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Page<PostDto> getPublishedPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByPublishedTrue(pageable);

        return PostDto.toDtoPage(posts);
    }

    public Page<PostDto> getPublishedPostsByUser(Pageable pageable, String author) {
        Page<Post> posts = postRepository.findAllByAuthor_UsernameAndPublishedTrue(pageable, author);

        return PostDto.toDtoPage(posts);
    }

    public Page<PostDto> getUserPosts(Pageable pageable, String author) {
        Page<Post> posts = postRepository.findAllByAuthor_Username(pageable, author);

        return PostDto.toDtoPage(posts);
    }

    public Page<PostDto> getRecent30Posts(Pageable pageable) {
        Page<Post> posts = postRepository.findTop30ByPublishedTrue(pageable);

        return PostDto.toDtoPage(posts);
    }

    public PostDto getNthPublishedPostByUser(Pageable pageable, String username, Integer order) {     // 특정 회원의 N번째 글 상세보기에 사용
        Page<Post> page = postRepository.findByAuthor_UsernameAndPublishedTrue(pageable, username);

        Optional<PostDto> postDtoOptional = page.stream()
                .findFirst()
                .map(PostDto::from);

        if (postDtoOptional.isPresent()) {
            return postDtoOptional.get();
        } else {
            throw new CustomException(NOT_FOUND_POST);
        }
    }

    @Transactional
    public void write(WriteForm writeForm, User user) {
        Optional<Member> authorOptional = memberRepository.findByUsername(user.getUsername());

        authorOptional.ifPresent(author -> {
            Post post = Post.builder()
                    .author(author)
                    .title(writeForm.getTitle())
                    .body(writeForm.getBody())
                    .published(writeForm.isPublished())
                    .paid(writeForm.isPaid())
                    .build();

            postRepository.save(post);
        });
    }

    public PostDto getPostDetail(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            return PostDto.from(post);
        } else {
            throw new CustomException(NOT_FOUND_POST);
        }
    }

    @Transactional
    public void modify(Long id, WriteForm writeForm) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.updatePost(
                    writeForm.getTitle(),
                    writeForm.getBody(),
                    writeForm.isPublished(),
                    writeForm.isPaid()
            );
        } else {
            throw new CustomException(NOT_FOUND_POST);
        }
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    public Page<PostDto> searchPosts(List<String> kwTypes, String kw, Pageable pageable) {
        return postRepository.search(kwTypes, kw, pageable);
    }
}
