package com.example.demo.repository;

import com.example.demo.entity.CommentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentInfoRepository extends MongoRepository<CommentInfo, Integer> {
}
