package com.ll.medium.domain.post.post;

import com.ll.medium.domain.post.post.controller.PostController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @DisplayName("글 작성 폼")
    void showWriteFoam() throws Exception {
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
}

