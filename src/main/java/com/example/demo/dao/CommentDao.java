package com.example.demo.dao;

import com.example.demo.dao.parent.ContentDao;
import com.example.demo.entity.Comment;
import com.example.demo.entity.CommentInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentDao extends ContentDao<Comment, CommentInfo> {
    List<Comment> findByDiscussId(int discussId, Pageable pageable);
}
