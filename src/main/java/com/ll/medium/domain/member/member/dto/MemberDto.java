package com.ll.medium.domain.member.member.dto;

import com.ll.medium.domain.member.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {
    private String username;
    private String email;
    private boolean verified;

    public static MemberDto from(Member member) {
        return MemberDto.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .verified(member.isVerified())
                .build();
    }
}
