package com.example.demo.repository;

import com.example.demo.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, Integer> {
    List<Audit> findByDoneEquals(boolean done);
}
