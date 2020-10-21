package com.example.demo.repository;

import com.example.demo.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Integer> {
    List<Interest> findByNameLike(String name);

    Optional<Interest> findByName(String name);

    List<Interest> findByParentId(int parentId);
}
