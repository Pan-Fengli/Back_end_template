//package com.example.demo.service;
//
//import com.example.demo.dto.TagItem;
//import com.example.demo.entity.Interest;
//import com.example.demo.entity.User;
//import com.example.demo.exception.MyException;
//import com.example.demo.repository.UserRepository;
//import org.junit.Assert;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class InterestServiceTest {
//
//    @Autowired
//    private InterestService interestService;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    @Transactional
//    public void getAll() throws MyException {
//        List<TagItem> array=interestService.getAll();
//        Assert.assertEquals("length",3,array.size());
//        TagItem data=array.get(0);
//        Assert.assertEquals("id",1,data.getId());
//        Assert.assertEquals("name","兴趣1",data.getName());
//    }
//
//    @Test
//    @Transactional
//    public void changeInterest() throws MyException {
//        List<Integer> List=new ArrayList<>();
//        List.add(1);
//        List.add(2);
//        List.add(3);
//        interestService.changeInterest(1,List);
//        User user=userRepository.getOne(1);
//        List<Interest> list= user.getInterestList();
//        Interest one=list.get(2);
//        Assert.assertEquals("length",3,list.size());
//        Assert.assertEquals("id",3,one.getId());
//        Assert.assertEquals("name","兴趣3",one.getName());
//        MyException exception = assertThrows(MyException.class, () -> {
//            interestService.changeInterest(100,List);
//        });
//        Assert.assertEquals("code",4,exception.getCode());
//        Assert.assertEquals("message","对象[User]找不到",exception.getMsg());
//
//        List.add(123);
//        exception = assertThrows(MyException.class, () -> {
//            interestService.changeInterest(1,List);
//        });
//        Assert.assertEquals("code",4,exception.getCode());
//        Assert.assertEquals("message","对象[Interest]找不到",exception.getMsg());
//
//
//    }
//}
