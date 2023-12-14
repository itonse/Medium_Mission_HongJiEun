package com.ll.medium.domain.post.post.service;

import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.dto.WriteForm;
import com.ll.medium.domain.post.post.entity.Post;
import com.ll.medium.domain.post.post.repository.PostRepository;
import com.ll.medium.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public List<PostDto> getPublishedPosts() {
        List<Post> posts = postRepository.findAllByOrderByIdDesc().stream()
                .filter(Post::isPublished)
                .toList();

        List<PostDto> postsDto = posts.stream()
                .map(this::convertToDto)    // Post객체를 PostDto로 변환
                .collect(Collectors.toList());

        return postsDto;
    }

    public List<PostDto> getMyPosts(String author) {
        List<Post> posts = postRepository.findAllByAuthor_UsernameOrderByIdDesc(author);

        List<PostDto> postsDto = posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return postsDto;
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
}
