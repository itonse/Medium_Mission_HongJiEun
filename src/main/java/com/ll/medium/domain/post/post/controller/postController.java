package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.WriteForm;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class postController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("posts", postService.getPosts());
        return "domain/post/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {
        return "domain/post/post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid WriteForm writeForm) {
        postService.write(writeForm, rq.getUser());
        return "redirect:/post/list";
    }
}
