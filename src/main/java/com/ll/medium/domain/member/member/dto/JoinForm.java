package com.ll.medium.domain.member.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinForm {
    @NotBlank
    @Size(min = 4, max = 12)
    private String username;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
    @NotBlank
    @Email
    private String email;
}
