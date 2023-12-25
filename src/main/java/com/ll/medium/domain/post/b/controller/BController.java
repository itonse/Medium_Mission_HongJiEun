package com.ll.medium.domain.post.b.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/b")
@RequiredArgsConstructor
public class BController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/{username}")
    public String getAllPostsByUser(@PathVariable("username") String username,
                                    Model model) {
        Page<PostDto> userPosts = postService.getUserPosts(username);
        model.addAttribute("postsDto", userPosts);

        return "domain/post/b/b_list";
    }

    @GetMapping("/{username}/{id}")
    public String getUserPostByOrder(@PathVariable("username") String username,
                                     @PathVariable("id") Integer id,
                                     Model model) {
        try {
            PostDto postDto = postService.getUserOrderedPost(username, id);
            model.addAttribute("postDto", postDto);

            return "domain/post/post/detail";
        } catch (NoSuchElementException e) {
            return rq.historyBack(e.getMessage());
        }
    }
}
