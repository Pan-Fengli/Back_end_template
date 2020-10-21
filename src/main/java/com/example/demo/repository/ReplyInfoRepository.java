package com.example.demo.repository;

import com.example.demo.entity.ReplyInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReplyInfoRepository extends MongoRepository<ReplyInfo,Integer> {
}
