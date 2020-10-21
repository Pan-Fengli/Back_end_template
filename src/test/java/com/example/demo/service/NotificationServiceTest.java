package com.example.demo.service;

import com.example.demo.DemoApplicationTests;
import com.example.demo.Enum.NotificationType;
import com.example.demo.exception.MyException;
import com.example.demo.repository.NotificationRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @Transactional
    public void add() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1 = assertThrows(MyException.class, () -> {
            notificationService.add(NotificationType.NTF_DELETE_DISCUSS, 200, 1,
                    null, null, null, null, null);
        });
        Assert.assertEquals(4, e1.getCode());
        MyException e2 = assertThrows(MyException.class, () -> {
            notificationService.add(NotificationType.NTF_DELETE_DISCUSS, 2, 1,
                    100, null, null, null, null);
        });
        Assert.assertEquals(4, e2.getCode());
        MyException e3 = assertThrows(MyException.class, () -> {
            notificationService.add(NotificationType.NTF_DELETE_COMMENT, 2, 1,
                    null, 100, null, null, null);
        });
        Assert.assertEquals(4, e3.getCode());
        //正确性检验
        Assert.assertEquals(3, notificationRepository.findAll().size());
        notificationService.add(NotificationType.NTF_DELETE_COMMENT, 2, 1,
                null, 1, null, null, null);
        Assert.assertEquals(4, notificationRepository.findAll().size());
    }

    @Test
    @Transactional
    public void getNotifications() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1 = assertThrows(MyException.class, () -> {
            notificationService.getNotifications(100);
        });
        Assert.assertEquals(4, e1.getCode());
        //正确性检验
        Assert.assertEquals(3, notificationService.getNotifications(1).size());
    }

    @Test
    @Transactional
    public void clearNotifications() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1 = assertThrows(MyException.class, () -> {
            notificationService.clearNotifications(100);
        });
        Assert.assertEquals(4, e1.getCode());
        //正确性检验
        Assert.assertEquals(3, notificationService.getNotifications(1).size());
        notificationService.clearNotifications(1);
        Assert.assertEquals(0, notificationService.getNotifications(1).size());
    }

    @Test
    @Transactional
    public void clearNotification() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1 = assertThrows(MyException.class, () -> {
            notificationService.clearNotification(100,1);
        });
        Assert.assertEquals(4, e1.getCode());
        //正确性检验
        Assert.assertEquals(3, notificationService.getNotifications(1).size());
        notificationService.clearNotification(1,1);
        Assert.assertEquals(2, notificationService.getNotifications(1).size());
    }
}
