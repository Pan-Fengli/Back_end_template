package com.example.demo.dao.impl;

import com.example.demo.dao.ReplyDao;
import com.example.demo.dao.parent.ContentDaoImpl;
import com.example.demo.entity.Reply;
import com.example.demo.entity.ReplyInfo;
import com.example.demo.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReplyDaoImpl extends ContentDaoImpl<Reply, ReplyInfo> implements ReplyDao {
    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public List<Reply> findByCommentId(int commentId, Pageable pageable) {
        return listAddInfo(replyRepository.findByCommentId(commentId, pageable).toList());
    }

    @Override
    protected ReplyInfo newContentInfo(Reply reply) {
        return new ReplyInfo(reply.getId(), reply.getDetail());
    }

    @Override
    protected void addInfoBy(Reply reply, ReplyInfo replyInfo) {
        reply.setDetail(replyInfo.getContent());
    }
}
