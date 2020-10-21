package com.example.demo.redis;

import com.example.demo.dao.DiscussDao;
import com.example.demo.entity.Discuss;
import com.example.demo.entity.UserDiscussLike;
import com.example.demo.repository.UserDiscussLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDiscussLikeRedis extends UserContentLikeRedis<UserDiscussLike> {
    @Autowired
    private UserDiscussLikeRepository userDiscussLikeRepository;
    @Autowired
    private DiscussDao discussDao;

    @Override
    protected String getName() {
        return "USER_DISCUSS_LIKE";
    }

    @Override
    protected List<UserDiscussLike> getByContentId(int contentId) {
        return userDiscussLikeRepository.findByDiscussId(contentId);
    }

    @Override
    protected UserDiscussLike newUserContentLike(int userId, int contentId, boolean isLike) {
        return new UserDiscussLike(userId, contentId, isLike);
    }

    @Override
    protected void deleteByIdList(List<Integer> deleteIdList) {
        for (int id : deleteIdList) {
            userDiscussLikeRepository.deleteById(id);
        }
    }

    @Override
    protected void addUserContentLike(List<UserDiscussLike> userContentLikeList) {
        userDiscussLikeRepository.saveAll(userContentLikeList);
    }

    @Override
    protected void saveContentLikeNum(int id, int likeN, int dislikeN) {
        Discuss discuss = discussDao.findByIdMySQL(id);
        discuss.setLikeNum(likeN);
        discuss.setDislikeNum(dislikeN);
        discussDao.saveMySQL(discuss);
    }
}
