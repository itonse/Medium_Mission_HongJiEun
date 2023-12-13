package com.ll.medium.domain.medium.medium.controller;

import com.ll.medium.domain.member.member.controller.MemberController;
import com.ll.medium.domain.member.member.entity.Member;
import com.ll.medium.domain.member.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
@AutoConfigureMockMvc    // 프로젝트 내붕 있는 Bean을 모두 등록(운영 환경과 유사)
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
        createAndSaveMember("existingUser", "11112222", "user@example.com");

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

    @Test   // GET /member/login
    @DisplayName("로그인 폼")
    void showlogin() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/login"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("domain/member/member/login"))
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(content().string(containsString("""
                        로그인
                        """.stripIndent().trim())));
    }

    @Test    // POST /member/login
    @DisplayName("로그인 성공")
    void SuccessLogin() throws Exception {
        // GIVEN
        createAndSaveMember("member1", "11112222", "new@example.com");

        // WHEN
        ResultActions resultActions = mvc.perform(post("/member/login")
                        .with(csrf())
                        .param("username", "member1")
                        .param("password", "11112222")
                )
                .andDo(print());

        MvcResult mvcResult = resultActions.andReturn();    // 요청의 결과 받아오기
        HttpSession session = mvcResult.getRequest().getSession(false);    // 요청으로부터 세션 객체 받아오기(새 새션이 생성되지 않도록 한다)
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");    // 세션에서 인증된 사용자 정보가 들어있는 SecurityContext 가져오기
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();     // 현재 인증된 사용자의 정보를 담고 있는 UserDetails 객체를 SecurityContext의 Authentication 객체로부터 추출

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection());

        assertThat(userDetails.getUsername()).isEqualTo("member1");
    }

    @Test
    @DisplayName("로그인 실패")
    void failLogin() throws Exception {
        // WHEN
        ResultActions resultActions = mvc.perform(post("/member/login")
                .with(csrf())
                .param("username", "existingUser")
                .param("password", "wrongPassword"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/member/login"));
    }

    private void createAndSaveMember(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .verified(true)
                .build();

        memberRepository.save(member);
    }
}
