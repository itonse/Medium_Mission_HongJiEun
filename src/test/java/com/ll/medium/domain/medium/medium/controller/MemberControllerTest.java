package com.ll.medium.domain.medium.medium.controller;

import com.ll.medium.domain.member.member.controller.MemberController;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class MemberControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test   // GET /member/join
    @DisplayName("회원가입 폼")
    void showJoin() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/join"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("domain/member/member/join"))
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showJoin"))
                .andExpect(content().string(containsString("""
                        회원가입
                        """.stripIndent().trim())));
    }

    @Test    // POST /member/join
    @DisplayName("회원가입 성공")
    void successJoin() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/member/join")
                                .with(csrf())
                                .param("username", "newUser")
                                .param("password", "123123123")
                                .param("passwordConfirm", "123123123")
                                .param("email", "new@example.com")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"));

        assertThat(memberRepository.existsByUsername("newUser")).isTrue();
    }

    @Test    // POST /member/join
    @DisplayName("회원가입 실패 - 이미 존재하는 아이디")
    void failJoinExistingUsername() throws Exception {
        // GIVEN
        Member member = Member.builder()
                .username("existingUser")
                .password(passwordEncoder.encode("11112222"))
                .email("user@example.com")
                .verified(false)
                .build();

        memberRepository.save(member);

        // WHEN
        ResultActions resultActions = mvc.perform(post("/member/join")
                .with(csrf())
                .param("username", "existingUser")
                .param("password", "123123123")
                .param("passwordConfirm", "123123123")
                .param("email", "existinguser@example.com"));

        // THEN
        resultActions
                .andExpect(view().name("domain/member/member/join"))
                .andExpect(model().attribute("errorMsg", "해당 아이디는 이미 사용중입니다."));
    }
}
