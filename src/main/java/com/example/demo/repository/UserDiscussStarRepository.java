package com.example.demo.repository;

import com.example.demo.entity.UserDiscussStar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDiscussStarRepository extends JpaRepository<UserDiscussStar, Integer> {
    UserDiscussStar findOneByUserIdAndDiscussId(int user_id, int discuss_id);

    Page<UserDiscussStar> findByUserId(int userId, Pageable pageable);
}
