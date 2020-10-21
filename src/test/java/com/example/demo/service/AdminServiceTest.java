package com.example.demo.service;

import com.example.demo.dto.DiscussItem;
import com.example.demo.dto.NumDate;
import com.example.demo.dto.UserItem;
import com.example.demo.entity.User;
import com.example.demo.exception.MyException;
import com.example.demo.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void getDiscussNum() throws ParseException {
        ParseException exception = assertThrows(ParseException.class, () -> {
            adminService.getDiscussNum("1", "32x");
        });
//        Assert.assertEquals("code",2,exception.getCode());
//        Assert.assertEquals("message","日期格式错误",exception.getMsg());
        List<NumDate> array = adminService.getDiscussNum("2020-07-23", "2020-08-23");
        Assert.assertEquals("length", 32, array.size());
        NumDate data = array.get(0);
        Assert.assertEquals("id", 0, data.getNum());
        Assert.assertEquals("name", "Thu Jul 23 00:00:00 CST 2020", data.getTime().toString());
    }

    @Test
    @Transactional
    public void getRegisterNum() throws ParseException {
        ParseException exception = assertThrows(ParseException.class, () -> {
            adminService.getRegisterNum("1", "32x");
        });
//        Assert.assertEquals("code",2,exception.getCode());
//        Assert.assertEquals("message","日期格式错误",exception.getMsg());
        List<NumDate> array = adminService.getDiscussNum("2020-07-23", "2020-07-23");
        Assert.assertEquals("length", 1, array.size());
        NumDate data = array.get(0);
        Assert.assertEquals("id", 0, data.getNum());
        Assert.assertEquals("name", "Thu Jul 23 00:00:00 CST 2020", data.getTime().toString());

        array = adminService.getDiscussNum("2020-07-23", "2020-08-30");
        Assert.assertEquals("length", 39, array.size());
        data = array.get(0);
        Assert.assertEquals("id", 0, data.getNum());
        Assert.assertEquals("name", "Thu Jul 23 00:00:00 CST 2020", data.getTime().toString());
    }

    @Test
    @Transactional
    public void setUserState() throws ParseException, MyException {
        MyException exception = assertThrows(MyException.class, () -> {
            adminService.setUserState(1221, 1, 1);
        });
        Assert.assertEquals("code", 4, exception.getCode());
        Assert.assertEquals("message", "对象[User]找不到", exception.getMsg());

        exception = assertThrows(MyException.class, () -> {
            adminService.setUserState(1, 1, 1);
        });
        Assert.assertEquals("code", 6, exception.getCode());
        Assert.assertEquals("message", "cannot set your self", exception.getMsg());

        exception = assertThrows(MyException.class, () -> {
            adminService.setUserState(1010, 1011, 1);
        });
        Assert.assertEquals("code", 6, exception.getCode());
        Assert.assertEquals("message", "权限错误", exception.getMsg());

        adminService.setUserState(1011, 1, 1);
        User info = userRepository.getOne(1);
        Assert.assertEquals("get info不对", 1, info.getState());

    }

    @Test
    public void getUserList() throws ParseException, MyException {

        MyException exception = assertThrows(MyException.class, () -> {
            adminService.getUserList("1", 2222, 10, 0);
        });
        Assert.assertEquals("code", 4, exception.getCode());
        Assert.assertEquals("message", "对象[User]找不到", exception.getMsg());

        exception = assertThrows(MyException.class, () -> {
            adminService.getUserList("1", 1, 10, 0);
        });
        Assert.assertEquals("code", 6, exception.getCode());
        Assert.assertEquals("message", "您的权限不够", exception.getMsg());

        List<UserItem> array = adminService.getUserList("1", 1011, 10, 0);
        Assert.assertEquals("length", 275, array.size());
        UserItem data = array.get(0);
        Assert.assertEquals("id", 1, data.getId());
        Assert.assertEquals("name", "user1", data.getUsername());

        array = adminService.getUserList("undefined", 1011, 10, 0);
        Assert.assertEquals("length", 1011, array.size());
        data = array.get(0);
        Assert.assertEquals("id", 1, data.getId());
        Assert.assertEquals("name", "user1", data.getUsername());

    }

    @Test
    @Transactional
    public void getDiscussList() throws ParseException, MyException {
        assertThrows(MyException.class, () -> {
            adminService.getDiscussList("1x23", "1x23", 3);
        });

        MyException exception = assertThrows(MyException.class, () -> {
            adminService.getDiscussList("2020-07-22", "2020-07-22", 3122);
        });
        Assert.assertEquals("code", 4, exception.getCode());
        Assert.assertEquals("message", "对象[User]找不到", exception.getMsg());
//
        exception = assertThrows(MyException.class, () -> {
            adminService.getDiscussList("2020-07-23", "2020-07-23", 1);
        });
        Assert.assertEquals("code", 6, exception.getCode());
        Assert.assertEquals("message", "您的权限不够", exception.getMsg());
//
        List<DiscussItem> array = adminService.getDiscussList("2020-07-23", "2020-08-30", 1011);
        Assert.assertEquals("length", 1000, array.size());
        DiscussItem data = array.get(0);
        Assert.assertEquals("id", 1, data.getId());
        Assert.assertEquals("name", "discuss1", data.getTitle());

        //普通用户：
//        Assert.assertEquals("普通用户",0,adminService.getDiscussList(2).size());
//        //小管理：
//        Assert.assertEquals("小管理",11,adminService.getDiscussList(16).size());
//        //大管理：
//        Assert.assertEquals("大管理",11,adminService.getDiscussList(1).size());

    }
}
