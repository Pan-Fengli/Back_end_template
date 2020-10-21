package com.example.demo.dao.impl;

import com.example.demo.dao.DiscussDao;
import com.example.demo.dao.parent.RichTextContentDaoImpl;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Discuss;
import com.example.demo.entity.DiscussInfo;
import com.example.demo.entity.Reply;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DiscussDaoImpl extends RichTextContentDaoImpl<Discuss, DiscussInfo> implements DiscussDao {
    @Autowired
    private DiscussRepository discussRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentInfoRepository commentInfoRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ReplyInfoRepository replyInfoRepository;

    @Override
    public List<Discuss> findAll() {
        return listAddInfo(jpaRepository.findAll());
    }

    @Override
    public List<Discuss> findAll(Pageable pageable) {
        return listAddInfo(jpaRepository.findAll(pageable).toList());
    }


    @Override
    protected DiscussInfo newContentInfo(Discuss discuss) {
        return new DiscussInfo(discuss.getId(), discuss.getDetail(), discuss.getText());
    }

    @Override
    protected void addInfoBy(Discuss discuss, DiscussInfo discussInfo) {
        discuss.setDetail(discussInfo.getContent());
        discuss.setText(discussInfo.getText());
    }

    @Override
    public void deleteById(int id) {
        List<Comment> commentList = commentRepository.findByDiscussId(id);
        for (Comment comment : commentList) {
            List<Reply> replies = replyRepository.findByCommentId(comment.getId());
            for (Reply reply : replies) {
                replyRepository.deleteById(reply.getId());
                replyInfoRepository.deleteById(reply.getId());
            }
            commentRepository.deleteById(comment.getId());
            commentInfoRepository.deleteById(comment.getId());
        }
        super.deleteById(id);
    }

    @Override
    public List<Discuss> findByUserId(int userId, Pageable pageable) {
        return listAddInfo(discussRepository.findByUserId(userId, pageable).toList());
    }

    @Override
    public int save(Discuss discuss) {
        discuss.setText(extractText(discuss.getDetail()));
        return super.save(discuss);
    }

    protected String extractText(String html) {
        return html.replaceAll("</?[^>]+>", "");
    }
}
