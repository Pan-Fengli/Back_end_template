package com.example.demo.service.impl;


import com.example.demo.Enum.NotificationType;
import com.example.demo.dao.CommentDao;
import com.example.demo.dao.DiscussDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.NotificationItem;
import com.example.demo.entity.*;
import com.example.demo.exception.MyException;
import com.example.demo.repository.AuditRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private DiscussDao discussDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AuditRepository auditRepository;

    @Override
    public void add(NotificationType type, Integer sender_id, Integer getter_id,
                    Integer discussId, Integer commentId, Integer auditId, Integer accept, String senderContent) throws MyException {
        User sender = userDao.findById(sender_id);
        User getter = userDao.findById(getter_id);
        if (sender == null || getter == null)
            throw new MyException(4, "对象[User]找不到");

        Notification notification = new Notification();
        notification.setRead(false);
        notification.setSenderId(sender_id);
        notification.setGetterId(getter_id);
        if (type == NotificationType.NTF_DELETE_DISCUSS) {
            Discuss discuss = discussDao.findById(discussId);
            if (discuss == null)
                throw new MyException(4, "对象[Discuss]找不到");
            notification.setContent("你的讨论[" + discuss.getTitle() + "]已被管理员删除");
            notificationRepository.saveAndFlush(notification);
        } else if (type == NotificationType.NTF_DELETE_COMMENT) {
            Comment comment = commentDao.findById(commentId);
            if (comment == null)
                throw new MyException(4, "对象[Comment]找不到");
            notification.setContent("你在讨论[" + discussDao.findById(comment.getDiscussId()).getTitle() + "]下的评论[" + comment.getDetail() + "]已被管理员删除");
            notificationRepository.saveAndFlush(notification);

        } else if (type == NotificationType.NTF_BAN_SPEECH) {
            notification.setContent("你已被管理员禁言");
            notificationRepository.saveAndFlush(notification);
        } else if (type == NotificationType.NTF_BAN_ACCOUNT) {
            notification.setContent("你已被管理员封号");
            notificationRepository.saveAndFlush(notification);
        } else if (type == NotificationType.NTF_RETURN_NORMAL) {
            notification.setContent("你的账号已被管理员恢复正常");
            notificationRepository.saveAndFlush(notification);
        } else if (type == NotificationType.NTF_AUDIT) {
            if (auditRepository.findById(auditId).isEmpty())
                throw new MyException(4, "对象[Audit]找不到");
            Audit audit = auditRepository.findById(auditId).get();
            String content = "你发起的请求:将用户[" + userDao.findById(audit.getUserId()).getUsername() + "]设为";
            if (audit.getState() == 0) content += "[正常]";
            else if (audit.getState() == 1) content += "[禁言]";
            else content += "[封号]";
            if (accept == 1) content += "审核通过";
            else content += "审核不通过,理由是[" + senderContent + "]";
            notification.setContent(content);
            notificationRepository.saveAndFlush(notification);
        }
    }

    @Override
    public List<NotificationItem> getNotifications(Integer uid) throws MyException {
        if (userDao.findById(uid) == null) throw new MyException(4, "对象[User]找不到");
        List<NotificationItem> list = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findByGetterId(uid);

        for (Notification notification : notifications) {
            if (!notification.isRead()) {
                list.add(toDTO(notification));
            }
        }
        return list;
    }

    private NotificationItem toDTO(Notification notification) {
        NotificationItem notificationItem = new NotificationItem(notification, userDao.findById(notification.getSenderId()), userDao.findById(notification.getGetterId()));
        return notificationItem;
    }

    @Override
    public void clearNotifications(Integer uid) throws MyException {
        if (userDao.findById(uid) == null) throw new MyException(4, "对象[User]找不到");
        List<Notification> notifications = notificationRepository.findByGetterId(uid);
        for (Notification notification : notifications) {
            if (!notification.isRead()) {
                notification.setRead(true);
                notificationRepository.saveAndFlush(notification);
            }
        }
    }

    @Override
    public void clearNotification(Integer nid, int userId) throws MyException {
        Optional<Notification> notificationOptional = notificationRepository.findById(nid);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            if (notification.getGetterId() != userId) {
                throw new MyException(4, "对象[Notification]找不到");
            }
            notification.setRead(true);
            notificationRepository.saveAndFlush(notification);
        } else throw new MyException(4, "对象[Notification]找不到");
    }
}
