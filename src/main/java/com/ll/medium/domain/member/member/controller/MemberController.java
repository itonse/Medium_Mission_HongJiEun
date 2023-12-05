package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.dto.JoinForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @PreAuthorize("isAnonymous()")    // 로그인하지 않은 상태여야 메서드에 접근 가능
    @GetMapping("/join")
    public String showJoin() {
        return "domain/member/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public void join(@Valid JoinForm joinForm) {
        // TODO
    }
}
