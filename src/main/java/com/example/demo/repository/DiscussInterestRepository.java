package com.example.demo.repository;

import com.example.demo.entity.DiscussInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface DiscussInterestRepository extends JpaRepository<DiscussInterest, Integer> {
    List<DiscussInterest> findByDiscussId(int discussId);

    List<DiscussInterest> findByInterestId(int interestId);

    @Modifying
    @Transactional
    void deleteByDiscussId(int discussId);
}
