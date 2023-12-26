package com.ll.medium.domain.post.blog.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/b")
@RequiredArgsConstructor
public class BlogController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/{username}")
    public String getPublishedPostsByUser(@RequestParam(defaultValue = "1") int page, @PathVariable("username") String username,
                                    Model model) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("createDate").descending());

        Page<PostDto> userPosts = postService.getPublishedPostsByUser(pageable, username);

        model.addAttribute("username", username);
        model.addAttribute("postsDto", userPosts);

        return "blog_list";
    }

    @GetMapping("/{username}/{id}")
    public String getNthPublishedPostByUser(@PathVariable("username") String username,
                                     @PathVariable("id") Integer id,
                                     Model model) {
        try {
            Pageable pageable = PageRequest.of(id - 1, 1, Sort.by("createDate"));

            PostDto postDto = postService.getNthPublishedPostByUser(pageable, username, id);
            model.addAttribute("postDto", postDto);

            return "domain/post/post/detail";
        } catch (NoSuchElementException e) {
            return rq.historyBack(e.getMessage());
        }
    }
}
