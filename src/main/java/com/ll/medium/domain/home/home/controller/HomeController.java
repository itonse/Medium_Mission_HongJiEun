package com.ll.medium.domain.home.home.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostService postService;

    @GetMapping("/")
    public String showMain(Model model) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createDate").descending());

        Page<PostDto> recent30Posts = postService.getRecent30Posts(pageable);
        model.addAttribute("postsDto", recent30Posts);

        return "domain/home/home/main";
    }
}
