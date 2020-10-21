package com.example.demo.service;

import com.example.demo.dto.AuditItem;
import com.example.demo.dto.RtnMsg;
import com.example.demo.exception.MyException;

import java.util.List;

public interface AuditService {
    List<AuditItem> getUndone(int userId) throws MyException;

    void process(int senderId, int auditId, boolean accept, String content) throws MyException;

    void postOne(int senderId, int userId, int state, String content) throws MyException;
}
