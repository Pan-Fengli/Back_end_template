package com.example.demo.service.impl;

import com.example.demo.Enum.NotificationType;
import com.example.demo.dao.CommentDao;
import com.example.demo.dao.DiscussDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.CommentItem;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Discuss;
import com.example.demo.entity.User;
import com.example.demo.entity.UserCommentLike;
import com.example.demo.exception.MyException;
import com.example.demo.redis.UserCommentLikeRedis;
import com.example.demo.repository.UserCommentLikeRepository;
import com.example.demo.service.CommentService;
import com.example.demo.service.NotificationService;
import com.example.demo.tmp.LikeNAndDislikeN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private DiscussDao discussDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserCommentLikeRepository userCommentLikeRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserCommentLikeRedis userCommentLikeRedis;

    private CommentItem toDTO(Comment comment, int userId) throws MyException {
        User user = userDao.findById(comment.getUserId());
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        LikeNAndDislikeN likeNAndDislikeN = userCommentLikeRedis.getLikeN(comment.getId());
        if (likeNAndDislikeN != null) {
            comment.setLikeNum(likeNAndDislikeN.getLikeN());
            comment.setLikeNum(likeNAndDislikeN.getDislikeN());
        }
        Boolean isLike = null;
        int redisLike = userCommentLikeRedis.findByContentIdAndUserId(comment.getId(), userId);
        if (redisLike == 2) {
            UserCommentLike userCommentLike = userCommentLikeRepository.findOneByUserIdAndCommentId(userId, comment.getId());
            if (userCommentLike != null) {
                isLike = userCommentLike.isLike();
            }
        }else if(redisLike!=3){
            isLike = redisLike == 1;
        }
        return new CommentItem(comment, user, isLike != null && isLike, isLike != null && !isLike);
    }

    private List<CommentItem> toDTOList(List<Comment> commentList, int userId) throws MyException {
        List<CommentItem> commentItemList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentItemList.add(toDTO(comment, userId));
        }
        return commentItemList;
    }

    @Override
    public int addOne(int userId, int discussId, String content) throws MyException {
        Discuss discuss = discussDao.findByIdMySQL(discussId);
        if (discuss == null) {
            throw new MyException(4, "对象[Discuss]找不到");
        }
        User user = userDao.findByIdMySQL(userId);
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        } else if (user.getState() != 0) {
            throw new MyException(5, "你已被禁言或封号");
        }
        discuss.addCommentNum();
        discussDao.saveMySQL(discuss);
        return commentDao.save(new Comment(userId, discussId, content));
    }

    @Override
    public void deleteOne(int senderId, int commentId) throws MyException {
        Comment comment = commentDao.findByIdMySQL(commentId);
        if (comment == null) {
            throw new MyException(4, "对象[Comment]找不到");
        }
        User sender = userDao.findByIdMySQL(senderId), user = userDao.findByIdMySQL(comment.getUserId());
        if (sender == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        if (sender.getRoot() == 0 && senderId != user.getId() || sender.getRoot() < user.getRoot()) {//普通用户不能删除别人的评论//小管理员不能删大管理员的评论
            throw new MyException(6, "你的权限不够");
        }
        if (senderId != user.getId()) {//管理员删除非自己的评论时,生成通知
            notificationService.add(NotificationType.NTF_DELETE_COMMENT, senderId, user.getId(), null, commentId, null, null, null);
        }
        Discuss discuss = discussDao.findByIdMySQL(comment.getDiscussId());
        discuss.subCommentNum();
        discussDao.saveMySQL(discuss);
        commentDao.deleteById(commentId);
    }

    @Override
    public List<CommentItem> getSome(int userId, int discussId, int pageIndex, int pageSize) throws MyException {
        Discuss discuss = discussDao.findById(discussId);
        if (discuss == null) {
            throw new MyException(4, "对象[Discuss]找不到");
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "time");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        List<Comment> commentList = commentDao.findByDiscussId(discussId, pageable);
        return toDTOList(commentList, userId);
    }

    @Override
    public void likeOrDislikeOne(int userId, int commentId, boolean isLike) throws MyException {
        userCommentLikeRedis.like(userId, commentId, isLike);
    }
}
