package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.dto.WriteForm;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import com.ll.medium.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Page<PostDto> getPublishedPosts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createDate"));
        Page<Post> posts = postRepository.findAllByPublishedTrue(pageable);

        Page<PostDto> postsDto = convertToDtos(posts);

        return postsDto;
    }

    public Page<PostDto> getUserPosts(String author) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createDate").descending());
        Page<Post> posts = postRepository.findAllByAuthor_Username(pageable, author);

        Page<PostDto> postsDto = convertToDtos(posts);

        return postsDto;
    }

    public Page<PostDto> getRecent30Posts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createDate"));
        Page<Post> posts = postRepository.findTop30ByOrderByIdDesc(pageable);

        Page<PostDto> postsDto = convertToDtos(posts);

        return postsDto;
    }

    public RsData<PostDto> getUserOrderedPost(String username, Integer order) {
        Pageable pageable = PageRequest.of(order - 1, 1);
        Page<Post> page = postRepository.findByAuthor_Username(username, pageable);

        Optional<PostDto> postDtoOptional = page.stream()
                .findFirst()
                .map(this::convertToDto);

        if (postDtoOptional.isPresent()) {
            return RsData.of("200", "success", postDtoOptional.get());
        } else {
            return RsData.of("404", "fail");
        }
    }

    @Transactional
    public RsData<Object> write(WriteForm writeForm, User user) {
        Optional<Member> authorOptional = memberRepository.findByUsername(user.getUsername());

        authorOptional.ifPresent(author -> {
            Post post = Post.builder()
                    .author(author)
                    .title(writeForm.getTitle())
                    .body(writeForm.getBody())
                    .published(writeForm.isPublished())
                    .build();

            postRepository.save(post);
        });

        return RsData.of("201", "글이 등록되었습니다.");
    }

    public RsData<PostDto> getPostDetail(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            PostDto postDto = convertToDto(post);

            return RsData.of("200", "success", postDto);
        } else {
            return RsData.of("404", "%d번 글이 존재하지 않습니다.".formatted(id));
        }
    }

    @Transactional
    public RsData<Object> modify(Long id, WriteForm writeForm) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setTitle(writeForm.getTitle());
            post.setBody(writeForm.getBody());
            post.setPublished(writeForm.isPublished());

            return RsData.of("200", "성공적으로 수정되었습니다.");
        } else {
            return RsData.of("404", "해당 글은 존재하지 않습니다.");
        }
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    private PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .published(post.isPublished())
                .author(post.getAuthor().getUsername())
                .createDate(post.getCreateDate())
                .build();
    }

    private Page<PostDto> convertToDtos(Page<Post> posts) {
        return posts.map(this::convertToDto);
    }
}
