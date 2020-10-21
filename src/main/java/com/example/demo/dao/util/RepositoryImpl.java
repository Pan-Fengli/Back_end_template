package com.example.demo.dao.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Resource;

public class RepositoryImpl<T> {
    @Resource
    private JpaRepository<T, Integer> jpaRepository;

}
