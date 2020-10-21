package com.example.demo.dao;

import com.example.demo.dao.parent.ContentDao;
import com.example.demo.entity.Reply;
import com.example.demo.entity.ReplyInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReplyDao extends ContentDao<Reply, ReplyInfo> {
    List<Reply> findByCommentId(int commentId, Pageable pageable);
}
