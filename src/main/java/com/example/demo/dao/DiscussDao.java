package com.example.demo.dao;

import com.example.demo.dao.parent.ContentDao;
import com.example.demo.entity.Discuss;
import com.example.demo.entity.DiscussInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiscussDao extends ContentDao<Discuss, DiscussInfo> {
    List<Discuss> findAll();

    List<Discuss> findAll(Pageable pageable);

    void deleteById(int id);

    List<Discuss> findByUserId(int userId, Pageable pageable);
}
