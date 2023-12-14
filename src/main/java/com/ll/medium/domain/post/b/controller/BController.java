package com.ll.medium.domain.post.b.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/b")
@RequiredArgsConstructor
public class BController {
    private final PostService postService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{username}")
    public String getAllPostsByUser(Model model) {
        String username = rq.getUser().getUsername();
        List<PostDto> userPosts = postService.getUserPosts(username);
        model.addAttribute("postsDto", userPosts);
        return "domain/post/post/list";
    }
}
