package com.example.demo.service;

import com.example.demo.dto.CommentItem;
import com.example.demo.dto.RtnMsg;
import com.example.demo.exception.MyException;

import java.util.List;

public interface CommentService {
    int addOne(int userId, int discussId, String content) throws MyException;

    void deleteOne(int senderId, int commentId) throws MyException;

    void likeOrDislikeOne(int userId, int commentId, boolean isLike) throws MyException;

    List<CommentItem> getSome(int userId, int discussId, int pageIndex, int pageSize) throws MyException;
}
