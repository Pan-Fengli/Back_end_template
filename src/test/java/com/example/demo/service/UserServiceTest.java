package com.example.demo.service;

import com.example.demo.DemoApplicationTests;
import com.example.demo.exception.MyException;
import com.example.demo.repository.UserRepository;
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
class UserServiceTest  {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void register() throws MyException {
        //异常检验
        //1.注册邮箱重复
        MyException e1=assertThrows(MyException.class,()->{userService.Register("u5","p5","e4","ph5");});
        Assert.assertEquals(12,e1.getCode());
        //正确性检验
        Assert.assertEquals(4,userRepository.findAll().size());
        Assert.assertEquals("u5",userService.Register("u5","p5","e5","ph5").getUsername());
        Assert.assertEquals(5,userRepository.findAll().size());
    }
}
