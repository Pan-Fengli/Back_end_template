package com.example.demo.service;

import com.example.demo.dto.ReplyItem;
import com.example.demo.dto.RtnMsg;
import com.example.demo.exception.MyException;

import java.util.List;


public interface ReplyService {
    int addOne(int userId, int commentId, int toReplyId, String content) throws MyException;

    void deleteOne(int userId, int replyId) throws MyException;

    List<ReplyItem> getSome(int commentId, int pageIndex, int pageSize) throws MyException;
}
