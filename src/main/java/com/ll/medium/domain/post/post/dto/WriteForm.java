package com.ll.medium.domain.post.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WriteForm {
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    private boolean published;
    private boolean paid;
}
