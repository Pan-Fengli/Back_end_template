package com.example.demo.dao;

import com.example.demo.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserDao {
    User findById(int id);

    User findByIdMySQL(int id);

    int save(User user);

    int saveMySQL(User user);

    User findByEmail(String email);

    List<User> findFollow(int id);

    List<User> findFollowed(int id);

    boolean checkFollow(int id, int fid);

    void cancelFollow(int id, int fid);

    List<User> findAll();

    List<User> findAll(Pageable pageable);

    void setIcon(int userId, String icon);
}
