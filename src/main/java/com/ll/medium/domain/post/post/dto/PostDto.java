package com.ll.medium.domain.post.post.dto;

import lombok.Builder;
import lombok.Getter;

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
}
