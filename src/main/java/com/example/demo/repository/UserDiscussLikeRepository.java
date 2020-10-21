package com.example.demo.repository;

import com.example.demo.entity.UserDiscussLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDiscussLikeRepository extends JpaRepository<UserDiscussLike, Integer> {
    UserDiscussLike findOneByUserIdAndDiscussId(int userId, int discussId);

    List<UserDiscussLike> findByDiscussId(int discussId);
}
