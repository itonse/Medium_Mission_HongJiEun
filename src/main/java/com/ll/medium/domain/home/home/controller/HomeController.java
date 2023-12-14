package com.ll.medium.domain.home.home.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostService postService;
    @GetMapping("/")
    public String showMain(Model model) {
        List<PostDto> recent30Posts = postService.getRecent30Posts();
        model.addAttribute("postsDto", recent30Posts);
        return "domain/post/post/list";
    }
}
