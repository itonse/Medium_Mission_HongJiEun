package com.ll.medium.domain.post.post.controller;

import com.ll.medium.domain.post.post.dto.PostDto;
import com.ll.medium.domain.post.post.dto.WriteForm;
import com.ll.medium.domain.post.post.service.PostService;
import com.ll.medium.global.exception.CustomException;
import com.ll.medium.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.ll.medium.global.exception.ErrorCode.*;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private static final String MEMBERSHIP_REQUIRED_MSG = "이 글은 유료멤버십전용 입니다.";
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("createDate").descending());

        Page<PostDto> publishedPosts = postService.getPublishedPosts(pageable);

        model.addAttribute("postDtoPage", publishedPosts);

        return "domain/post/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myList")
    public String getMyPosts(@RequestParam(defaultValue = "1") int page, Model model) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by("createDate").descending());

        String author = rq.getUser().getUsername();
        Page<PostDto> myPosts = postService.getUserPosts(pageable, author);

        if (myPosts.isEmpty()) {
            throw new CustomException(MEMBER_POSTS_NOT_FOUND);
        } else {

            model.addAttribute("postDtoPage", myPosts);

            return "domain/post/post/myList";
        }
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
        PostDto postDto = postService.getPostDetail(id);

        if (postDto.isPaid() && !rq.getReq().isUserInRole("PAID")) {
            if (rq.isLogout() || !postDto.getAuthor().equals(rq.getUser().getUsername())) {
                postDto.setBody(MEMBERSHIP_REQUIRED_MSG);
            }
        }

        model.addAttribute("postDto", postDto);

        return "domain/post/post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable("id") Long id, Model model) {
        PostDto postDto = postService.getPostDetail(id);
        String author = postDto.getAuthor();

        if (!author.equals(rq.getUser().getUsername())) {
            throw new CustomException(EDIT_PERMISSION_DENIED);
        }

        model.addAttribute("postDto", postDto);

        return "domain/post/post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/modify")
    public String modifyPost(@Valid WriteForm writeForm,
                             @PathVariable("id") Long id, Model model) {

        postService.modify(id, writeForm);
        model.addAttribute("id", id);

        return rq.redirect("/post/{id}", "성공적으로 수정되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        PostDto postDto = postService.getPostDetail(id);
        String author = postDto.getAuthor();

        if (!author.equals(rq.getUser().getUsername())) {
            throw new CustomException(DELETE_PERMISSION_DENIED);
        }

        postService.delete(id);

        return rq.redirect("/post/list", "글을 삭제하였습니다.");
    }
}
