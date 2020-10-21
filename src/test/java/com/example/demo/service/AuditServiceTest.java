package com.example.demo.service;

import com.example.demo.DemoApplicationTests;
import com.example.demo.dto.AuditItem;
import com.example.demo.entity.Audit;
import com.example.demo.exception.MyException;
import com.example.demo.repository.AuditRepository;
import com.example.demo.service.AuditService;
import com.example.demo.service.SelfService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuditServiceTest {

    @Autowired
    private AuditService auditService;
    @Autowired
    private AuditRepository auditRepository;

    @Test
    void getUndone() throws MyException {
        MyException exception = assertThrows(MyException.class, () -> {
            auditService.getUndone(10000);
        });
        Assert.assertEquals("code", 4, exception.getCode());
        Assert.assertEquals("message", "对象[User]找不到", exception.getMsg());

        exception = assertThrows(MyException.class, () -> {
            auditService.getUndone(1);
        });
        Assert.assertEquals("code", 6, exception.getCode());
        Assert.assertEquals("message", "你的权限不够", exception.getMsg());

        List<AuditItem> array = auditService.getUndone(1011);
        Assert.assertEquals("size", 10, array.size());
    }

    @Test
    @Transactional
    void process() throws MyException {
        auditService.postOne(1010, 1, 1, "no reason");

        List<Audit> Array = auditRepository.findAll();
        Audit audit = Array.get(0);
        int id = audit.getId();

        auditService.process(1011, id, true, "yes reason");

        List<Audit> array = auditRepository.findAll();
        Audit info = array.get(0);
        Assert.assertTrue("get done", info.isDone());

        MyException exception = assertThrows(MyException.class, () -> {
            auditService.process(2, 233, true, "yes reason");
        });
        Assert.assertEquals("code", 4, exception.getCode());
        Assert.assertEquals("message", "对象[Audit]找不到", exception.getMsg());

        exception = assertThrows(MyException.class, () -> {
            auditService.process(1011, id, true, "yes reason");
        });
        Assert.assertEquals("code", 13, exception.getCode());
        Assert.assertEquals("message", "already done", exception.getMsg());
    }

    @Test
    @Transactional
    void postOne() throws MyException {
        MyException exception = assertThrows(MyException.class, () -> {
            auditService.postOne(1012, 1, 1, "no");
        });
        Assert.assertEquals("code", 4, exception.getCode());
        Assert.assertEquals("message", "对象[User]找不到", exception.getMsg());

        exception = assertThrows(MyException.class, () -> {
            auditService.postOne(1010, 1010, 1, "no");
        });
        Assert.assertEquals("code", 6, exception.getCode());
        Assert.assertEquals("message", "无法处理你自己", exception.getMsg());

        auditService.postOne(1010, 1, 1, "no reason");

    }
}
