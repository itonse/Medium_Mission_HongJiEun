package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.dto.JoinForm;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<?> join(JoinForm joinForm) {
        if (memberRepository.existsByUsername(joinForm.getUsername())) {
            return RsData.of("F-1", "해당 아이디는 이미 사용중입니다.");
        }

        Member newMember = Member.builder()
                .username(joinForm.getUsername())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .email(joinForm.getEmail())
                .verified(false)
                .build();

        memberRepository.save(newMember);

        return RsData.of("S-1");
    }
}
