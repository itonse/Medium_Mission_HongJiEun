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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public List<Post> getPublishedPosts() {
        return postRepository.findAll().stream()
                .filter(Post::isPublished)
                .toList();
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
                    .build();

            postRepository.save(post);
        });
    }

    public RsData getPostDetail(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            PostDto postDto = PostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .body(post.getBody())
                    .published(post.isPublished())
                    .author(post.getAuthor().getUsername())
                    .createDate(post.getCreateDate())
                    .build();

            return RsData.of("200", "success", postDto);
        } else {
            return RsData.of("404", "%d번 글이 존재하지 않습니다.".formatted(id));
        }
    }
}
