package com.example.demo.repository;

import com.example.demo.entity.UserUserStar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserUserStarRepository extends JpaRepository<UserUserStar,Integer> {
    List<UserUserStar> findByUserId(int userId);
    List<UserUserStar> findByStarUserId(int starUserId);
    UserUserStar findOneUserUserStarByStarUserIdAndUserId(int fid,int id);
}
