package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.DemoApplicationTests;
import com.example.demo.dto.DiscussItem;
import com.example.demo.dto.NotificationItem;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.NotificationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MessageCenterControllerTest{
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;

    //不用body传参就用null,用body则jsonObject传参
    private JSONObject getPostResult(String url, JSONObject jsonObject) throws Exception {
        String string;
        if (jsonObject == null) string = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        else string = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return (JSONObject) JSON.parse(string);
    }

    private JSONObject getGetResult(String url) throws Exception {
        return (JSONObject) JSON.parse(mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    void getNotifications() throws Exception {
        //异常参数是否存在检验
        JSONObject result1 = getGetResult("/messageCenter/notifications");
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        JSONObject result2 = getGetResult("/messageCenter/notifications?userId=abc");
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        JSONObject result3 = getGetResult("/messageCenter/notifications?userId=100");
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        JSONObject result4 = getGetResult("/messageCenter/notifications?userId=1");
        List<NotificationItem> list = om.readValue(result4.getString("data"), new TypeReference<List<NotificationItem>>() {
        });
        Assert.assertEquals(0, result4.get("code"));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    void clearNotifications() throws Exception {
        //异常参数是否存在检验
        JSONObject result1 = getGetResult("/messageCenter/clearNotifications");
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        JSONObject result2 = getGetResult("/messageCenter/clearNotifications?userId=abc");
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        JSONObject result3 = getGetResult("/messageCenter/clearNotifications?userId=100");
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        Assert.assertEquals(0, notificationService.getNotifications(1).size());
        JSONObject result4 = getGetResult("/messageCenter/clearNotifications?userId=1");
        Assert.assertEquals(0, result4.get("code"));
        Assert.assertEquals(0, notificationService.getNotifications(1).size());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    void clearNotification() throws Exception {
        //异常参数是否存在检验
        JSONObject result1 = getGetResult("/messageCenter/clearNotification");
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        JSONObject result2 = getGetResult("/messageCenter/clearNotification?nId=abc&userId=1");
        System.out.println(result2);
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        JSONObject result3 = getGetResult("/messageCenter/clearNotification?nId=100&userId=1");
        Assert.assertEquals(4, result3.get("code"));

        JSONObject result4 = getGetResult("/messageCenter/clearNotification?nId=100&userId=12");
        Assert.assertEquals(14, result4.get("code"));

    }
}
