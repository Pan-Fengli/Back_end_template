package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.DemoApplicationTests;
import com.example.demo.entity.Interest;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInterest;
import com.example.demo.repository.InterestRepository;
import com.example.demo.repository.UserInterestRepository;
import com.example.demo.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class InterestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInterestRepository userInterestRepository;

    @Autowired
    private UserRepository userRepository;

    private JSONObject getGetResult(String url) throws Exception {
        MvcResult result = mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");

        return JSONObject.parseObject(result.getResponse().getContentAsString());
    }

    private JSONObject getPostResult(String url, JSONObject jsonObject) throws Exception {
        if (jsonObject == null) {
            MvcResult result = mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk()).andReturn();
            result.getResponse().setCharacterEncoding("UTF-8");
            return JSONObject.parseObject(result.getResponse().getContentAsString());
        } else {
            MvcResult result = mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                    .andExpect(status().isOk()).andReturn();
            result.getResponse().setCharacterEncoding("UTF-8");
            return JSONObject.parseObject(result.getResponse().getContentAsString());
        }
    }


    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    void getChildren() throws Exception {

        //无参数捕获
        JSONObject testjson = getGetResult("/interest/getChildren");
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数错误捕获
        testjson = getGetResult("/interest/getChildren?id=sb");
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //正确性
        testjson = getGetResult("/interest/getChildren?id=2");
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    void getOne() throws Exception {

        //无参数捕获
        JSONObject testjson = getGetResult("/interest/getOne");
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数错误捕获
        testjson = getGetResult("/interest/getOne?id=sb");
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //正确性
        testjson = getGetResult("/interest/getOne?id=2");
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    void getSearch() throws Exception {

        //无参数捕获
        JSONObject testjson = getGetResult("/interest/search");
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //空搜索结果
        testjson = getGetResult("/interest/search?key=sb");
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));

        //非空
        testjson = getGetResult("/interest/search?key=乒乓球");
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));

    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    void add() throws Exception {
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/interest/add")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/interest/add")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        JSONObject item = new JSONObject();
        JSONObject testjson = getPostResult("/interest/add", item);
        System.out.println(testjson);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        item.put("name", "测试兴趣");
        item.put("id", 3);
        testjson = getPostResult("/interest/add", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        Assert.assertNotNull("data", testjson.get("data"));

    }


    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    void setInterest() throws Exception {
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/interest/set")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/interest/set")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"username\":}";//非法json字符串应该被code=8捕获(因为这里用的不是map...)
        MvcResult result = mockMvc.perform(post("/interest/set")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 8, testjson.get("code"));
        Assert.assertEquals("message", "body格式错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));


        //参数缺失
        JSONObject item = new JSONObject();
        item.put("userId", 1);
        testjson = getPostResult("/interest/set", item);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数错误
        item = new JSONObject();
        item.put("userId", "1xsd");//一个就可以了,因为在检查null之前先parse了
        testjson = getPostResult("/interest/set", item);
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数正确-添加兴趣
        item = new JSONObject();
        item.put("userId", 1);//一个就可以了,因为在检查null之前先parse了
        item.put("interestId", 2);//变成123加了一个3
        item.put("type", 0);
        testjson = getPostResult("/interest/set", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        List<UserInterest> userInterests = userInterestRepository.findByUserId(1);
        Assert.assertEquals("length", 5, userInterests.size());
//
//        //业务逻辑
//        item = new JSONObject();
//        item.put("userId", "11");
//        item.put("idList", "[1,2,3]");//变成123加了一个3
//        testjson = getPostResult("/interest/set", item);
//        Assert.assertEquals("code", 4, testjson.get("code"));
//        Assert.assertEquals("message", "对象[User]找不到", testjson.get("message"));
//        Assert.assertNull("data", testjson.get("data"));
//
//        item = new JSONObject();
//        item.put("userId", "1");
//        item.put("idList", "[1,2,33]");
//        testjson = getPostResult("/interest/set", item);
//        Assert.assertEquals("code", 4, testjson.get("code"));
//        Assert.assertEquals("message", "对象[Interest]找不到", testjson.get("message"));
//        Assert.assertNull("data", testjson.get("data"));

    }
}
