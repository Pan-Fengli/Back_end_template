package com.example.demo.dao.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfo;
import com.example.demo.entity.UserUserStar;
import com.example.demo.repository.UserInfoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserUserStarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserUserStarRepository userUserStarRepository;

    private List<User> listAddIcon(List<User> userList) {
        for (User user : userList) {
            addIcon(user);
        }
        return userList;
    }

    private User addIcon(User user) {
        int id = user.getId();
        Optional<UserInfo> userInfo = userInfoRepository.findById(id);
        if (userInfo.isEmpty()) {
            user.setIcon("");
        } else {
            user.setIcon(userInfo.get().getIcon());
        }
        return user;
    }

    @Override
    public User findById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        }
        return addIcon(user.get());
    }

    @Override
    public User findByIdMySQL(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        }
        return user.get();
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return null;
        }
        return addIcon(user.get());
    }

    @Override
    public int save(User user) {
        int id = userRepository.saveAndFlush(user).getId();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setIcon(user.getIcon());
        userInfoRepository.save(userInfo);
        return id;
    }

    @Override
    public int saveMySQL(User user) {
        return userRepository.save(user).getId();
    }

    @Override
    public List<User> findFollow(int userId) {
        List<UserUserStar> userUserStarList = userUserStarRepository.findByUserId(userId);
        List<User> followUserList = new ArrayList<>();
        for (UserUserStar userUserStar : userUserStarList) {
            followUserList.add(userRepository.findById(userUserStar.getStarUserId()).get());
        }
        return followUserList;
    }

    @Override
    public List<User> findFollowed(int userId) {
        List<UserUserStar> userUserStarList = userUserStarRepository.findByStarUserId(userId);
        List<User> followUserList = new ArrayList<>();
        for (UserUserStar userUserStar : userUserStarList) {
            followUserList.add(userRepository.findById(userUserStar.getId()).get());
        }
        return followUserList;
    }

    @Override
    public boolean checkFollow(int id, int fid) {
        UserUserStar check = userUserStarRepository.findOneUserUserStarByStarUserIdAndUserId(fid, id);
        return check != null;//找不到——可能是没有人，也可能是没有关注
    }

    @Override
    public void cancelFollow(int id, int fid) {
        UserUserStar check = userUserStarRepository.findOneUserUserStarByStarUserIdAndUserId(fid, id);
        userUserStarRepository.delete(check);
    }

    @Override
    public List<User> findAll() {
        return listAddIcon(userRepository.findAll());
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        return listAddIcon(userRepository.findAll(pageable).toList());
    }

    @Override
    public void setIcon(int userId, String icon) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);
        if (userInfoOptional.isEmpty()) {
            return;
        }
        UserInfo userInfo = userInfoOptional.get();
        userInfo.setIcon(icon);
        userInfoRepository.save(userInfo);
    }
}
