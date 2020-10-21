package com.example.demo.repository;

import com.example.demo.entity.Discuss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscussRepository extends JpaRepository<Discuss, Integer> {
    List<Discuss> findByUserId(int userId);

    List<Discuss> findAll();

    Page<Discuss> findByUserId(int userId, Pageable pageable);

    Page<Discuss> findAll(Pageable pageable);
}
