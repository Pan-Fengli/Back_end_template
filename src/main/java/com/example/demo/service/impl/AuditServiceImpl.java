package com.example.demo.service.impl;

import com.example.demo.Enum.NotificationType;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.AuditItem;
import com.example.demo.entity.Audit;
import com.example.demo.entity.User;
import com.example.demo.exception.MyException;
import com.example.demo.repository.AuditRepository;
import com.example.demo.service.AuditService;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuditRepository auditRepository;
    @Autowired
    private NotificationService notificationService;

    private List<AuditItem> toDTOList(List<Audit> auditList) throws MyException {
        List<AuditItem> auditItemList = new ArrayList<>();
//        if(auditList==null)
//        {
//            throw new MyException(4,"对象[audit]找不到");
//        }就算是空也只是长度为0，不会是null
        for (Audit audit : auditList) {
            auditItemList.add(new AuditItem(audit, userDao.findById(audit.getSenderId()), userDao.findById(audit.getUserId())));
        }
        return auditItemList;
    }

    @Override
    public List<AuditItem> getUndone(int userId) throws MyException {
        User admin = userDao.findById(userId);
        if (admin == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        if (admin.getRoot() != 2) {
            throw new MyException(6, "你的权限不够");
        }
        List<Audit> audits = auditRepository.findByDoneEquals(false);
        return toDTOList(audits);
    }

    @Override
    public void process(int senderId, int auditId, boolean accept, String content) throws MyException {
        Optional<Audit> auditOptional = auditRepository.findById(auditId);
        if (auditOptional.isEmpty()) {
//            return new RtnMsg<>(0, "no such audit");
            throw new MyException(4, "对象[Audit]找不到");
        }
        Audit audit = auditOptional.get();
        if (!audit.isDone()) {
            audit.setDone(true);
            auditRepository.save(audit);
            User auditSender = userDao.findById(audit.getSenderId());
            User user = userDao.findById(audit.getUserId());
            user.setWaiting(false);
            if (accept) {
                user.setState(audit.getState());
                //发消息
                //给小管理员发通知
                notificationService.add(NotificationType.NTF_AUDIT, senderId, auditSender.getId(), null, null, auditId, 1, null);
                //给用户发通知
                NotificationType type;
                if (audit.getState() == 0) type = NotificationType.NTF_RETURN_NORMAL;
                else if (audit.getState() == 1) type = NotificationType.NTF_BAN_SPEECH;
                else type = NotificationType.NTF_BAN_ACCOUNT;
                notificationService.add(type, auditSender.getId(), user.getId(), null, null, null, null, null);
            } else {
                //发消息，内容部分在content
                //只给小管理员发通知
                notificationService.add(NotificationType.NTF_AUDIT, senderId, auditSender.getId(), null, null, auditId, 0, content);
            }
            userDao.save(user);
//            return new RtnMsg<>(1, "success");
        } else {
//            return new RtnMsg<>(0,"already done");
            throw new MyException(13, "already done");
        }
    }

    @Override
    public void postOne(int senderId, int userId, int state, String content) throws MyException { // 提交一个audit
        if (senderId == userId) {
            throw new MyException(6, "无法处理你自己");
        }
        User sender = userDao.findById(senderId);
        if (sender == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        User user = userDao.findById(userId);
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        if (sender.getRoot() < user.getRoot()) {
            throw new MyException(6, "无法处理权限比你高的用户");
        }
        if (sender.getRoot() != 1) {  // 只有小管理员可以提交
//            return new RtnMsg<>(0, "root wrong");
            throw new MyException(6, "您的权限不正确");
        }
        if (user.isWaiting()) { // 已经在审核了
//            return new RtnMsg<>(0, "the user has an audit in waiting");——这个在测试中暂时没有覆盖到...
            throw new MyException(13, "the user has an audit in waiting");
        }
        user.setWaiting(true);
        Audit audit = new Audit();
        audit.setSenderId(senderId);
        audit.setUserId(userId);
        audit.setContent(content);
        audit.setState(state);
        audit.setDone(false);
        auditRepository.saveAndFlush(audit);
    }
}
