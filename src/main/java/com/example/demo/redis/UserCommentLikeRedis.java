package com.example.demo.redis;

import com.example.demo.dao.CommentDao;
import com.example.demo.entity.Comment;
import com.example.demo.entity.UserCommentLike;
import com.example.demo.repository.UserCommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCommentLikeRedis extends UserContentLikeRedis<UserCommentLike> {
    @Autowired
    private UserCommentLikeRepository userCommentLikeRepository;
    @Autowired
    private CommentDao commentDao;

    @Override
    protected String getName() {
        return "USER_COMMENT_LIKE";
    }

    @Override
    protected List<UserCommentLike> getByContentId(int contentId) {
        return userCommentLikeRepository.findByCommentId(contentId);
    }

    @Override
    protected UserCommentLike newUserContentLike(int userId, int contentId, boolean isLike) {
        return new UserCommentLike(userId, contentId, isLike);
    }

    @Override
    protected void deleteByIdList(List<Integer> deleteIdList) {
        for (int id : deleteIdList) {
            userCommentLikeRepository.deleteById(id);
        }
    }

    @Override
    protected void addUserContentLike(List<UserCommentLike> userContentLikeList) {
        userCommentLikeRepository.saveAll(userContentLikeList);
    }

    @Override
    protected void saveContentLikeNum(int id, int likeN, int dislikeN) {
        Comment comment = commentDao.findByIdMySQL(id);
        comment.setLikeNum(likeN);
        comment.setDislikeNum(dislikeN);
        commentDao.saveMySQL(comment);
    }
}
