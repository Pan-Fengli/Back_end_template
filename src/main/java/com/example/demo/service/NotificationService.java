package com.example.demo.service;


import com.example.demo.dto.NotificationItem;
import com.example.demo.Enum.NotificationType;
import com.example.demo.exception.MyException;

import java.util.List;

public interface NotificationService {
    void add(NotificationType type, Integer sender_id, Integer getter_id,
             Integer discussId, Integer commentId, Integer auditId, Integer accept,String senderContent) throws MyException;

    List<NotificationItem> getNotifications(Integer uid) throws MyException;

    void clearNotifications(Integer uid) throws MyException;

    void clearNotification(Integer nid,int userId) throws MyException;
}
