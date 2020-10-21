package com.example.demo.service;

import com.example.demo.DemoApplicationTests;
import com.example.demo.entity.Interest;
import com.example.demo.exception.MyException;
import com.example.demo.repository.DiscussRepository;
import com.example.demo.repository.UserDiscussStarRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DiscussServiceTest {
    @Autowired
    private DiscussService discussService;

    @Autowired
    private DiscussRepository discussRepository;
    @Autowired
    private UserDiscussStarRepository userDiscussStarRepository;

    @Test
    @Transactional
    public void getOne() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1=assertThrows(MyException.class,()->{discussService.getOne(100,1);});
        Assert.assertEquals(4,e1.getCode());
        MyException e2=assertThrows(MyException.class,()->{discussService.getOne(1,100);});
        Assert.assertEquals(4,e2.getCode());
        //正确性检验
        Assert.assertEquals(1,discussService.getOne(1,1).getId());
    }

    @Test
    @Transactional
    public void getSome() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1=assertThrows(MyException.class,()->{discussService.getSome("1",10,0,100,"my");});
        Assert.assertEquals(4,e1.getCode());
        //2.参数错误
        MyException e2=assertThrows(MyException.class,()->{discussService.getSome("",10,0,1,"xyz");});
        Assert.assertEquals(3,e2.getCode());
        //正确性检验
        Assert.assertEquals(1,discussService.getSome("1",10,0,1,"my").size());
    }

    @Test
    @Transactional
    public void addOne() throws MyException {
        //异常检验
        //1.找不到对象
        List<Integer> interests=new ArrayList<>();
        interests.add(1);
        interests.add(2);
        MyException e1=assertThrows(MyException.class,()->{discussService.addOne("讨论4","讨论4内容",100,interests);});
        Assert.assertEquals(4,e1.getCode());
        //2.检查禁言或封号
        MyException e2=assertThrows(MyException.class,()->{discussService.addOne("讨论4","讨论4内容",4,interests);});
        Assert.assertEquals(5,e2.getCode());
        //正确性检验
        Assert.assertEquals("讨论4",discussRepository.findById(discussService.addOne("讨论4","讨论4内容",1,interests)).get().getTitle());
    }

    @Test
    @Transactional
    public void likeOrDislikeOne() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1=assertThrows(MyException.class,()->{discussService.likeOrDislikeOne(100,3,true);});
        Assert.assertEquals(4,e1.getCode());
        MyException e2=assertThrows(MyException.class,()->{discussService.likeOrDislikeOne(1,300,true);});
        Assert.assertEquals(4,e2.getCode());
        //正确性检验
        Assert.assertEquals(0,discussRepository.findById(3).get().getLikeNum());
        discussService.likeOrDislikeOne(1,3,true);
        Assert.assertEquals(1,discussRepository.findById(3).get().getLikeNum());
    }

    @Test
    @Transactional
    public void starOne() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1=assertThrows(MyException.class,()->{discussService.starOne(100,3);});
        Assert.assertEquals(4,e1.getCode());
        MyException e2=assertThrows(MyException.class,()->{discussService.starOne(1,300);});
        Assert.assertEquals(4,e2.getCode());
        //正确性检验
        Assert.assertEquals(3,userDiscussStarRepository.findAll().size());
        discussService.starOne(1,3);
        Assert.assertEquals(4,userDiscussStarRepository.findAll().size());
    }

    @Test
    @Transactional
    public void deleteOne() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1=assertThrows(MyException.class,()->{discussService.deleteOne(100,3);});
        Assert.assertEquals(4,e1.getCode());
        MyException e2=assertThrows(MyException.class,()->{discussService.deleteOne(2,300);});
        Assert.assertEquals(4,e2.getCode());
        //2.权限检验
        MyException e3=assertThrows(MyException.class,()->{discussService.deleteOne(1,3);});
        Assert.assertEquals(6,e3.getCode());
        //正确性检验
        Assert.assertEquals(3,discussRepository.findAll().size());
        discussService.deleteOne(2,3);
        Assert.assertEquals(2,discussRepository.findAll().size());
    }
}
