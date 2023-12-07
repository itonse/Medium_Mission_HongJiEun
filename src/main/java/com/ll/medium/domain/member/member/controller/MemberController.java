package com.ll.medium.domain.member.member.controller;

import com.ll.medium.domain.member.member.dto.JoinForm;
import com.ll.medium.domain.member.member.service.MemberService;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")    // 200 OK
    public String showJoin() {
        return "domain/member/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")    // 302 Found
    public String join(@Valid JoinForm joinForm, Model model) {
        RsData<?> joinRs = memberService.join(joinForm);
        if (joinRs.getResultCode().equals("F-1")) {
            model.addAttribute("errorMsg", joinRs.getMsg());
            return "domain/member/member/join";
        }
        return "redirect:/";
    }


}
