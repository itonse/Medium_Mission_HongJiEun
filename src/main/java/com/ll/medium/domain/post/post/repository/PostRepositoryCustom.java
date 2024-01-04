package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.post.post.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    Page<PostDto> search(List<String> kwTypes, String kw, Pageable pageable);
}
