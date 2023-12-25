package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.dto.JoinForm;
import com.ll.medium.domain.member.member.dto.MemberDto;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rq.Rq;
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
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")    // 200 OK
    public String showJoin() {
        return "domain/member/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")    // 302 Found
    public String join(@Valid JoinForm joinForm) {
        try {
            MemberDto memberDto = memberService.join(joinForm);

            return rq.redirect("/", memberDto.getUsername() + "님 가입을 축하합니다!");
        } catch (IllegalStateException e) {
            return rq.historyBack(e.getMessage());
        }
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    String login() {
        return "domain/member/member/login";
    }
}
