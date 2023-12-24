package com.ll.medium.domain.post.post.repository;

import com.ll.medium.domain.post.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByPublishedTrue(Pageable pageable);

    Page<Post> findAllByAuthor_Username(Pageable pageable, String author);

    Page<Post> findTop30ByPublishedTrue(Pageable pageable);

    Page<Post> findByAuthor_Username(String username, Pageable pageable);
}
