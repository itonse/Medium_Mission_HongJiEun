package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.dto.WriteForm;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import com.ll.medium.global.rsData.RsData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/list")
    public String list(Model model) {
        List<PostDto> publishedPosts = postService.getPublishedPosts();
        model.addAttribute("postsDto", publishedPosts);
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
        RsData<Object> rsData = postService.write(writeForm, rq.getUser());
        return rq.redirect("/post/list", rsData.getMsg());
    }

    @GetMapping("/{id}")
    public String postDetail(@PathVariable("id") Long id, Model model) {

        RsData<PostDto> rsData = postService.getPostDetail(id);

        if (rsData.isFail()) {
            return rq.historyBack(rsData.getMsg());
        } else {
            model.addAttribute("postDto", rsData.getData());
            return "domain/post/post/detail";
        }
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable("id") Long id, Model model) {
        RsData<PostDto> rsData = postService.getPostDetail(id);
        if (rsData.isFail()) {
            return rq.historyBack(new Exception("해당 번호의 글이 존재하지 않습니다."));
        }

        String author = rsData.getData().getAuthor();
        if(!author.equals(rq.getUser().getUsername())) {
            return rq.historyBack(new Exception("수정권한이 없습니다."));
        }

        model.addAttribute("postDto", rsData.getData());
        return "domain/post/post/modify";
    }
}
