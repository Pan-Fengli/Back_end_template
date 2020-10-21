package com.example.demo.service;

import com.example.demo.dto.DiscussItem;
import com.example.demo.exception.MyException;

import java.util.List;

public interface DiscussService {
    DiscussItem getOne(int userId, int discussId) throws MyException;

    List<DiscussItem> getSome(String searchText, int pageSize, int pageIndex, int userId, String type) throws MyException;

    int addOne(String title, String content, int userId, List<Integer> interestList) throws MyException;

    void likeOrDislikeOne(int userId, int discussId, boolean isLike) throws MyException;

    void starOne(int userId, int discussId) throws MyException;

    void deleteOne(int senderId, int discussId) throws MyException;
}
