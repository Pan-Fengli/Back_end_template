package com.example.demo.repository;


import com.example.demo.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByDiscussId(int discussId);

    Page<Comment> findByDiscussId(int discussId, Pageable pageable);
}
