package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class postController {
    private final PostService postService;

    @GetMapping("/list")
    public String list() {
        return "domain/post/post/list";
    }
}
