package com.example.demo.repository;

import com.example.demo.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Integer> {
    List<UserInterest> findByUserId(int userId);

    @Transactional
    void deleteByUserIdAndInterestId(int userId, int interestId);
}
