package com.example.demo.repository;

import com.example.demo.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    List<Reply> findByCommentId(int commentId);

    Page<Reply> findByCommentId(int commentId, Pageable pageable);
}
