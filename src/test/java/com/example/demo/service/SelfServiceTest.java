package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.DemoApplicationTests;
import com.example.demo.dto.*;
import com.example.demo.entity.Interest;
import com.example.demo.entity.User;
import com.example.demo.entity.UserUserStar;
import com.example.demo.exception.MyException;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserUserStarRepository;
import com.example.demo.service.SelfService;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SelfServiceTest {
    @Autowired
    private SelfService selfService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUserStarRepository userUserStarRepository;

    @Test
    @Transactional
    public void testEditSelf() throws MyException {
        MyException exception = assertThrows(MyException.class, () -> {
            selfService.EditSelf(100,"黑人","ok","test@taoxq.com","男","M78","10086","test");

        });
        Assert.assertEquals("code",4,exception.getCode());
        Assert.assertEquals("message","对象[User]找不到",exception.getMsg());

        selfService.EditSelf(1,"黑人","ok","test@taoxq.com","男","M78","10086","test");
        User info= userRepository.getOne(1);
        Assert.assertEquals("get info不对",1,info.getId());
        Assert.assertEquals("get name不对","黑人",info.getUsername());
        Assert.assertEquals("get icon","ok",info.getIcon());
        Assert.assertEquals("get email","test@taoxq.com",info.getEmail());
        Assert.assertEquals("get gender","男",info.getGender());
        Assert.assertEquals("get home","M78",info.getHometown());
        Assert.assertEquals("get phone","10086",info.getPhoneNumber());
        Assert.assertEquals("get intro","test",info.getIntro());
    }//

    @Test//
    public void testGetInfoById() throws MyException {

        MyException exception = assertThrows(MyException.class, () -> {
            selfService.GetOneById(100);
        });
        Assert.assertEquals("code",4,exception.getCode());
        Assert.assertEquals("message","对象[User]找不到",exception.getMsg());

//        SelfInfo info=new SelfInfo(user);
        SelfInfo info=selfService.GetOneById(1);
        Assert.assertEquals("id",1,info.getId());
        Assert.assertEquals("root",0,info.getIsAdmin());
        Assert.assertEquals("username","u1",info.getUsername());
        Assert.assertEquals("password","p1",info.getPassword());
        Assert.assertEquals("email","e1",info.getEmail());
        Assert.assertEquals("gender","0",info.getGender());
        Assert.assertEquals("hometown","h1",info.getHometown());
        Assert.assertEquals("phoneNumber","ph1",info.getPhoneNumber());
        Assert.assertEquals("intro","in1",info.getIntro());
//        Assert.assertEquals("icon","",info.getIcon());

        //emmm这里预期的返回结果可能还要搞一搞....
        //Assert.assertTrue("不一样", info.isSame(selfService.findonebyid(2)));
    }

    @Test
    @Transactional//
    public void testCollectionList() throws MyException {
//        MyException exception = assertThrows(MyException.class, () -> {
//            selfService.FindCollection(100);
//        });
//        Assert.assertEquals("code",4,exception.getCode());
//        Assert.assertEquals("message","对象[User]找不到",exception.getMsg());
//
//        //insert into user_discuss+star value(1,1,2,current_time,0)
//        List<DiscussItem> array=selfService.FindCollection(2);
//        Assert.assertEquals("length",1,array.size());
//        DiscussItem data=array.get(0);
//        UserItem user=data.getUser();
//        Assert.assertEquals("id",3,data.getId());
//        Assert.assertEquals("uid",2,user.getId());
    }
    @Test
    @Transactional//
    public void testInterestList() throws MyException {
        MyException exception = assertThrows(MyException.class, () -> {
            selfService.findInterest(100);
        });
        Assert.assertEquals("code",4,exception.getCode());
        Assert.assertEquals("message","对象[User]找不到",exception.getMsg());

        //insert into user_discuss+star value(1,1,2,current_time,0)
        List<TagItem> array=selfService.findInterest(1);
        Assert.assertEquals("length",2,array.size());
        TagItem data=array.get(0);
        Assert.assertEquals("id",1,data.getId());
        Assert.assertEquals("name","兴趣1",data.getName());
    }
    @Test
    @Transactional//
    public void testFollowList() throws MyException {
        MyException exception = assertThrows(MyException.class, () -> {
            selfService.findInterest(100);
        });
        Assert.assertEquals("code",4,exception.getCode());
        Assert.assertEquals("message","对象[User]找不到",exception.getMsg());

        //insert into user_discuss+star value(1,1,2,current_time,0)
        List<SelfHead> array=selfService.FindFollow(1);
        Assert.assertEquals("length",2,array.size());
        SelfHead data=array.get(0);
        Assert.assertEquals("id",2,data.getId());
        Assert.assertEquals("name","u2",data.getUsername());
    }

    @Test
    @Transactional
    public void testCancelFollow() throws MyException {
        MyException exception = assertThrows(MyException.class, () -> {
            selfService.CancelFollow(1,222);

        });
        Assert.assertEquals("code",10,exception.getCode());
        Assert.assertEquals("message","尚未关注",exception.getMsg());

        selfService.CancelFollow(1,2);//emmm覆盖率在这里会要考虑，因为我后端做的检查其实比较多，需要每种情况都跑到
        //checkfollow应该是false
        UserUserStar check = userUserStarRepository.findOneUserUserStarByStarUserIdAndUserId(2, 1);
        Assert.assertNull("找不到该对象",check);
    }
    @Test
    @Transactional
    public void testChceckFollow()
    {
        //selfService.cancelfollow(1,12);//emmm覆盖率在这里会要考虑，因为我后端做的检查其实比较多，需要每种情况都跑到
        //checkfollow应该是false
        Assert.assertTrue(selfService.CheckFollow(1, 2));
        Assert.assertFalse(selfService.CheckFollow(1, 12));
    }
    @Test
    @Transactional
    public void testFollow() throws MyException {
        MyException exception = assertThrows(MyException.class, () -> {
            selfService.Follow(1,2);

        });
        Assert.assertEquals("code",10,exception.getCode());
        Assert.assertEquals("message","已经关注",exception.getMsg());
        //selfService.cancelfollow(1,12);//emmm覆盖率在这里会要考虑，因为我后端做的检查其实比较多，需要每种情况都跑到
        //checkfollow应该是false
        exception = assertThrows(MyException.class, () -> {
            selfService.Follow(1,222);

        });
        Assert.assertEquals("code",4,exception.getCode());
        Assert.assertEquals("message","对象[User]找不到",exception.getMsg());

        selfService.Follow(2,3);//emmm覆盖率在这里会要考虑，因为我后端做的检查其实比较多，需要每种情况都跑到
        //checkfollow应该是false
        UserUserStar check = userUserStarRepository.findOneUserUserStarByStarUserIdAndUserId(3, 2);
        Assert.assertNotNull("找到了该对象",check);
//
//        Assert.assertEquals("already",result.getMsg(),selfService.Follow(1,12).getMsg());
//        Assert.assertEquals("not yet",result1.getMsg(),selfService.Follow(1,3).getMsg());
    }

//    @Test
//    @Transactional
//    public void testCancelCollect()
//    {
//        //insert into user_discuss+star value(1,1,2,current_time,0)
//
//        selfService.CancelCollection(1,2);
//        Assert.assertEquals(1,selfService.CancelCollection(1,2).getCode());
//    }

}
