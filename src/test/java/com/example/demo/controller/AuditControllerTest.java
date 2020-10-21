package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.DemoApplicationTests;
import com.example.demo.entity.Audit;
import com.example.demo.entity.User;
import com.example.demo.repository.AuditRepository;
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
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuditRepository auditRepository;

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
    @WithUserDetails(value = "b1@qq.com")
    public void getList() throws Exception {
        //缺少参数
        JSONObject json_test = getGetResult("/audit/list");
        Assert.assertEquals("code", 1, json_test.get("code"));
        Assert.assertEquals("message", "缺少参数", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/audit/list?userId=1x23");
        Assert.assertEquals("code", 3, json_test.get("code"));
        Assert.assertEquals("message", "参数错误", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/audit/list?userId=3");
        Assert.assertEquals("code", 14, json_test.get("code"));
        Assert.assertEquals("message", "用户出错", json_test.get("message"));

        //参数正确
        json_test = getGetResult("/audit/list?userId=1011");
        Assert.assertEquals("code", 0, json_test.get("code"));
        Assert.assertEquals("message", "success", json_test.get("message"));
        JSONArray array = (JSONArray) json_test.get("data");
        //先看看会不会有null的那个异常
        Assert.assertEquals("size", 10, array.size());


    }

    @Test
    @Transactional
    @WithUserDetails(value = "a10@qq.com")
    void postOne() throws Exception {
        //先测一些特殊情况（post body特有的）
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/audit/one")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/audit/one")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"senderId\":}";//非法json字符串
        mockMvc.perform(post("/audit/one")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());

        teststring = "{}";
        MvcResult result = mockMvc.perform(post("/audit/one")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        JSONObject item = new JSONObject();
        item.put("senderId", "1010");
        testjson = getPostResult("/audit/one", item);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        item = new JSONObject();
        item.put("senderId", "2x1");
        item.put("userId", "2x1");
        item.put("state", "2x1");
        item.put("content", "2x1");
        testjson = getPostResult("/audit/one", item);
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        item = new JSONObject();
        item.put("senderId", "1010");
        item.put("userId", "3");
        item.put("state", "1");
        item.put("content", "no reason");
        testjson = getPostResult("/audit/one", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        item = new JSONObject();
        item.put("senderId", "1010");
        item.put("userId", "1011");
        item.put("state", "1");
        item.put("content", "no reason");
        testjson = getPostResult("/audit/one", item);
        Assert.assertEquals("code", 6, testjson.get("code"));
        Assert.assertEquals("message", "无法处理权限比你高的用户", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
        //从repository里面获取出数据
        List<Audit> array = auditRepository.findAll();
        Audit info = array.get(0);
//        Audit info= auditRepository.getOne(1);//插入了一个
//        Assert.assertEquals("get info不对",1,info.getId());
        Assert.assertEquals("userId 不对", 855, info.getUserId());
        Assert.assertEquals("senderId 不对", 1005, info.getSenderId());
        Assert.assertEquals("state 不对", 1, info.getState());
        Assert.assertEquals("理由不对", "audit content 1", info.getContent());

//
        item = new JSONObject();
        item.put("senderId", "1010");
        item.put("userId", "1010");
        item.put("state", "1");
        item.put("content", "no reason");
        testjson = getPostResult("/audit/one", item);
        Assert.assertEquals("code", 6, testjson.get("code"));
        Assert.assertEquals("message", "无法处理你自己", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
//        //注意，这里还有一个已经在审核没有测试，需要添加数据


    }

    @Test
    @Transactional
    @WithUserDetails(value = "b1@qq.com")
    void process() throws Exception {
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/audit/process")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/audit/process")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"senderId\":}";//非法json字符串
        mockMvc.perform(post("/audit/process")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());

        teststring = "{}";
        MvcResult result = mockMvc.perform(post("/audit/process")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        JSONObject item = new JSONObject();
        item.put("senderId", "2");
        testjson = getPostResult("/audit/process", item);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
        item = new JSONObject();
        item.put("senderId", "2x1");
        item.put("auditId", "2x1");
        item.put("accept", "2x1");
        item.put("content", "2x1");
        testjson = getPostResult("/audit/process", item);
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        item = new JSONObject();
        item.put("senderId", "2");
        item.put("auditId", 1);
        item.put("accept", "true");//——本来应该有true和false两种的
        item.put("content", "yes reason");
        testjson = getPostResult("/audit/process", item);
        Assert.assertEquals("code", 14, testjson.get("code"));
        Assert.assertEquals("message", "用户出错", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
        item = new JSONObject();
        item.put("senderId", "1011");
        item.put("auditId", 1);
        item.put("accept", "true");//——本来应该有true和false两种的
        item.put("content", "yes reason");
        testjson = getPostResult("/audit/process", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //从repository里面获取出数据
        List<Audit> array = auditRepository.findAll();
        Audit info = array.get(0);
//        Audit info= auditRepository.getOne(1);//插入了一个
//        Assert.assertEquals("get info不对",1,info.getId());
        Assert.assertTrue("get done", info.isDone());
//        //其实单元测试还要测有没有发过去信息...
//
////        Assert.assertEquals("get icon",2,info.getSender().getId());
////        Assert.assertEquals("get email",1,info.getState());
////        Assert.assertEquals("get gender","no reason",info.getContent());

        //业务逻辑
        item = new JSONObject();
        item.put("senderId", "1011");
        item.put("auditId", "233");
        item.put("accept", "true");//——本来应该有true和false两种的
        item.put("content", "yes reason");
        testjson = getPostResult("/audit/process", item);
        Assert.assertEquals("code", 4, testjson.get("code"));
        Assert.assertEquals("message", "对象[Audit]找不到", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));


    }
}
