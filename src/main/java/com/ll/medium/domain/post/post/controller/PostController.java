package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.dto.WriteForm;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/list")
    public String list(Model model) {
        Page<PostDto> publishedPosts = postService.getPublishedPosts();
        model.addAttribute("postsDto", publishedPosts);

        return "domain/post/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myList")
    public String getMyPosts(Model model) {
        String author = rq.getUser().getUsername();
        Page<PostDto> myPosts = postService.getUserPosts(author);
        model.addAttribute("postsDto", myPosts);

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

        return rq.redirect("/post/list", "글이 등록되었습니다.");
    }

    @GetMapping("/{id}")
    public String postDetail(@PathVariable("id") Long id, Model model) {
        try {
            PostDto postDto = postService.getPostDetail(id);
            model.addAttribute("postDto", postDto);

            return "domain/post/post/detail";
        } catch (NoSuchElementException e) {
            return rq.historyBack(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable("id") Long id, Model model) {
        try {
            PostDto postDto = postService.getPostDetail(id);
            String author = postDto.getAuthor();

            if (!author.equals(rq.getUser().getUsername())) {
                return rq.historyBack(new Exception("수정권한이 없습니다."));
            }

            model.addAttribute("postDto", postDto);

            return "domain/post/post/modify";
        } catch (NoSuchElementException e) {
            return rq.historyBack(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/modify")
    public String modifyPost(@Valid WriteForm writeForm,
                             @PathVariable("id") Long id, Model model) {
        try {
            postService.modify(id, writeForm);
            model.addAttribute("id", id);

            return rq.redirect("/post/{id}", "성공적으로 수정되었습니다.");
        } catch (NoSuchElementException e) {
            return rq.redirect("/post/list", e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        try {
            PostDto postDto = postService.getPostDetail(id);
            String author = postDto.getAuthor();

            if (!author.equals(rq.getUser().getUsername())) {
                return rq.historyBack(new Exception("삭제권한이 없습니다."));
            }

            postService.delete(id);

            return rq.redirect("/post/list", "글을 삭제하였습니다.");
        } catch (NoSuchElementException e) {
            return rq.historyBack(new Exception("해당 번호의 글이 존재하지 않습니다."));
        }
    }
}
