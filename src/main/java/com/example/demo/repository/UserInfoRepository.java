package com.example.demo.repository;

import com.example.demo.entity.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserInfoRepository extends MongoRepository<UserInfo,Integer> {
}
