package com.example.demo.dao.impl;

import com.example.demo.dao.CommentDao;
import com.example.demo.dao.parent.RichTextContentDaoImpl;
import com.example.demo.entity.Comment;
import com.example.demo.entity.CommentInfo;
import com.example.demo.entity.Reply;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ReplyInfoRepository;
import com.example.demo.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentDaoImpl extends RichTextContentDaoImpl<Comment, CommentInfo> implements CommentDao {
    @Autowired
    protected ReplyRepository replyRepository;
    @Autowired
    protected ReplyInfoRepository replyInfoRepository;
    @Autowired
    protected CommentRepository commentRepository;

    @Override
    public List<Comment> findByDiscussId(int discussId, Pageable pageable) {
        return listAddInfo(commentRepository.findByDiscussId(discussId, pageable).toList());
    }

    @Override
    protected CommentInfo newContentInfo(Comment comment) {
        return new CommentInfo(comment.getId(), comment.getDetail());
    }

    @Override
    protected void addInfoBy(Comment comment, CommentInfo commentInfo) {
        comment.setDetail(commentInfo.getContent());
    }

    @Override
    public void deleteById(int id) {
        List<Reply> replies = replyRepository.findByCommentId(id);
        for (Reply reply : replies) {
            replyRepository.deleteById(reply.getId());
            replyInfoRepository.deleteById(reply.getId());
        }
        super.deleteById(id);
    }
}
