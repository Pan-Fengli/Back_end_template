package com.example.demo.service;

import com.example.demo.DemoApplicationTests;
import com.example.demo.exception.MyException;
import com.example.demo.repository.ReplyRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
class ReplyServiceTest {
    @Autowired
    private ReplyService replyService;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    @Transactional
    public void addOne() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1=assertThrows(MyException.class,()->{replyService.addOne(100,1,2,"1给2的回复");});
        Assert.assertEquals(4,e1.getCode());
        MyException e2=assertThrows(MyException.class,()->{replyService.addOne(1,100,2,"1给2的回复");});
        Assert.assertEquals(4,e2.getCode());
        //2.封号或禁言检查
        MyException e3=assertThrows(MyException.class,()->{replyService.addOne(4,1,2,"1给2的回复");});
        Assert.assertEquals(5,e3.getCode());
        //正确性检验
        Assert.assertEquals(5,replyRepository.findAll().size());
        Assert.assertEquals(true,replyRepository.findById(replyService.addOne(1,1,2,"1给2的回复")).isPresent());
        Assert.assertEquals(6,replyRepository.findAll().size());
    }

    @Test
    @Transactional
    public void deleteOne() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1=assertThrows(MyException.class,()->{replyService.deleteOne(100,1);});
        Assert.assertEquals(4,e1.getCode());
        MyException e2=assertThrows(MyException.class,()->{replyService.deleteOne(1,100);});
        Assert.assertEquals(4,e2.getCode());
        //2.权限检查
        MyException e3=assertThrows(MyException.class,()->{replyService.deleteOne(2,1);});
        Assert.assertEquals(6,e3.getCode());
        //正确性检验
        Assert.assertEquals(5,replyRepository.findAll().size());
        replyService.deleteOne(1,1);
        Assert.assertEquals(4,replyRepository.findAll().size());
    }

    @Test
    @Transactional
    public void getSome() throws MyException {
        //异常检验
        //1.找不到对象
        MyException e1=assertThrows(MyException.class,()->{replyService.getSome(100,0,10);});
        Assert.assertEquals(4,e1.getCode());
        //正确性检验
        Assert.assertEquals(4,replyService.getSome(1,0,10).size());
    }
}
