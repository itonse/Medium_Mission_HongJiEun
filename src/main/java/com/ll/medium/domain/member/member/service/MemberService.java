package com.ll.medium.domain.member.member.service;

import com.ll.medium.domain.member.member.dto.JoinForm;
import com.ll.medium.domain.member.member.dto.MemberDto;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import com.ll.medium.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ll.medium.global.exception.ErrorCode.ALREADY_EXIST_USERNAME;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDto join(JoinForm joinForm) {
        checkUsername(joinForm.getUsername());

        Member newMember = Member.builder()
                .username(joinForm.getUsername())
                .password(passwordEncoder.encode(joinForm.getPassword()))
                .email(joinForm.getEmail())
                .verified(false)
                .build();

        memberRepository.save(newMember);

        return MemberDto.from(newMember);
    }

    public void checkUsername(String inputUsername) {
        if (memberRepository.existsByUsername(inputUsername)) {
            throw new CustomException(ALREADY_EXIST_USERNAME);
        }
    }
}
