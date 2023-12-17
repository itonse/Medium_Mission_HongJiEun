package com.ll.medium.domain.post.post;

import com.ll.medium.domain.post.post.controller.PostController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

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
public class PostControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test   // GET /member/write
    @WithMockUser(username = "author1", roles = {"USER"})    // 테스트를 위해 가짜 유저를 등록해서 인증정보 설정
    @DisplayName("글 작성 폼에 접근")
    void showWriteForm() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/post/write"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("domain/post/post/write"))
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("showWrite"))
                .andExpect(content().string(containsString("""
                        글 작성
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("글 작성 폼에 접근 실패 - 로그인하지 않은 상태")
    void failWriteForm() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/post/write"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("showWrite"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/member/login"));
    }

    @Test
    @WithMockUser(username = "author1", roles = {"USER"})
    @DisplayName("글 작성 성공")
    void successWrite() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/post/write")
                        .with(csrf())
                        .param("title", "제목1")
                        .param("body", "내용1")
                        .param("published", "true")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/post/list*"));
    }

}

