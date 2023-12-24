package com.ll.medium.domain.post.post.dto;

import com.ll.medium.domain.post.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String body;
    private boolean published;
    private String author;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public static PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .published(post.isPublished())
                .author(post.getAuthor().getUsername())
                .createDate(post.getCreateDate())
                .modifyDate(post.getModifyDate())
                .build();
    }

    /*
    public static List<PostDto> toDtoList(List<Post> posts) {
        return posts.stream()
                .map(PostDto::toDto)
                .toList();    // java 16 이상 부터 .collect(Collectors.toList()) 대신 사용
    }
    */

    public static Page<PostDto> toDtoPage(Page<Post> posts) {
        return posts
                .map(PostDto::toDto);    // Page가 제공하는 메서드 (stream 사용X)
    }
}
