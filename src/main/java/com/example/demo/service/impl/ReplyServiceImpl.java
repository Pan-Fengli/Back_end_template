package com.example.demo.service.impl;

import com.example.demo.dao.CommentDao;
import com.example.demo.dao.ReplyDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.ReplyItem;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Reply;
import com.example.demo.entity.User;
import com.example.demo.exception.MyException;
import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentDao commentDao;

    private ReplyItem toDTO(Reply reply) {
        User user = userDao.findById(reply.getUserId());
        Reply toReply = reply.getToReplyId() == 0 ? null : replyDao.findById(reply.getToReplyId());
        User toUser = toReply == null ? null : userDao.findById(toReply.getUserId());
        return new ReplyItem(reply, user, toReply, toUser);
    }

    private List<ReplyItem> toDTOList(List<Reply> replyList) {
        List<ReplyItem> replyItems = new ArrayList<>();
        for (Reply reply : replyList) {
            replyItems.add(toDTO(reply));
        }
        return replyItems;
    }

    @Override
    public int addOne(int userId, int commentId, int toReplyId, String content) throws MyException {
        User user = userDao.findByIdMySQL(userId);
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        } else if (user.getState() != 0) {
            throw new MyException(5, "你已被禁言或封号");
        }
        if (toReplyId != 0) {
            Reply toReply = replyDao.findByIdMySQL(toReplyId);
            if (toReply == null) {
                throw new MyException(4, "对象[Reply]找不到");
            }
        }
        Comment comment = commentDao.findByIdMySQL(commentId);
        if (comment == null) {
            throw new MyException(4, "对象[Comment]找不到");
        }
        comment.addReplyNum();
        commentDao.saveMySQL(comment);
        Reply reply = new Reply(commentId, userId, toReplyId, content);
        return replyDao.save(reply);
    }

    @Override
    public void deleteOne(int senderId, int replyId) throws MyException {
        Reply reply = replyDao.findByIdMySQL(replyId);
        if (reply == null) {
            throw new MyException(4, "对象[Reply]找不到");
        }
        User sender = userDao.findByIdMySQL(senderId), user = userDao.findByIdMySQL(reply.getUserId());
        if (sender == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        if (reply.getUserId() != senderId && sender.getRoot() == 0 || sender.getRoot() < user.getRoot()) {
            throw new MyException(6, "你的权限不够");
        }
        if (senderId != user.getId()) {

        }
        Comment comment = commentDao.findById(reply.getCommentId());
        comment.subReplyNum();
        commentDao.saveMySQL(comment);
        replyDao.deleteById(replyId);
    }

    @Override
    public List<ReplyItem> getSome(int commentId, int pageIndex, int pageSize) throws MyException {
        Comment comment = commentDao.findByIdMySQL(commentId);
        if (comment == null) {
            throw new MyException(4, "对象[Comment]找不到");
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "time");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        List<Reply> replyList = replyDao.findByCommentId(commentId, pageable);
        return toDTOList(replyList);
    }
}
