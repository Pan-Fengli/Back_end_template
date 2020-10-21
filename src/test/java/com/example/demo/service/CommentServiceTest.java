package com.example.demo.service;

import com.example.demo.DemoApplicationTests;
import com.example.demo.exception.MyException;
import com.example.demo.repository.CommentRepository;
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
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Transactional
    public void addOne() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1 = assertThrows(MyException.class, () -> {
            commentService.addOne(10010, 2, "abc");
        });
        Assert.assertEquals(4, e1.getCode());
        //2.禁言或封号
        MyException e2 = assertThrows(MyException.class, () -> {
            commentService.addOne(4, 2, "abc");
        });
        Assert.assertEquals(5, e2.getCode());
        //正确性检验
        Assert.assertEquals(true, commentRepository.findById(commentService.addOne(2, 2, "abc")).isPresent());
    }

    @Test
    @Transactional
    public void deleteOne() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1 = assertThrows(MyException.class, () -> {
            commentService.deleteOne(10000, 1);
        });
        Assert.assertEquals(4, e1.getCode());
        //2.权限检验
        MyException e2 = assertThrows(MyException.class, () -> {
            commentService.deleteOne(1, 1);
        });
        Assert.assertEquals(6, e2.getCode());
        //正确性检验
        int size = commentRepository.findAll().size();
        commentService.deleteOne(1011, 1);
        Assert.assertEquals(size - 1, commentRepository.findAll().size());
    }

    @Test
    @Transactional
    public void likeOrDislikeOne() throws MyException {
        //异常检验
        //1.找不到对象
//        MyException e1 = assertThrows(MyException.class, () -> {
//            commentService.likeOrDislikeOne(10000, 2, true);
//        });
//        Assert.assertEquals(4, e1.getCode());
//        MyException e2 = assertThrows(MyException.class, () -> {
//            commentService.likeOrDislikeOne(1, 100, true);
//        });
//        Assert.assertEquals(4, e2.getCode());
        //test of redis
        int size = commentRepository.findById(2).get().getLikeNum();
        commentService.likeOrDislikeOne(2, 2, true);
        Assert.assertEquals(size, commentRepository.findById(2).get().getLikeNum());
    }

    @Test
    @Transactional
    public void getSome() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1 = assertThrows(MyException.class, () -> {
            commentService.getSome(100000, 1, 0, 10);
        });
        Assert.assertEquals(4, e1.getCode());
        MyException e2 = assertThrows(MyException.class, () -> {
            commentService.getSome(1, 10000, 0, 10);
        });
        Assert.assertEquals(4, e2.getCode());
        //正确性检验
        Assert.assertEquals(3, commentService.getSome(1, 1, 0, 10).size());
    }
}
