package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.DemoApplicationTests;
import com.example.demo.dao.UserDao;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest{
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

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
    public void register() throws Exception {
        //异常参数是否存在检验
        JSONObject post = new JSONObject();
        JSONObject result1 = getPostResult("/user/register", post);
        Assert.assertEquals(1, result1.get("code"));
        //正确性检验
        post.put("userName", "u5");
        post.put("password", "p5");
        post.put("email", "e5");
        post.put("phoneNumber", "ph5");
        Assert.assertEquals(1011, userRepository.findAll().size());
        JSONObject result2 = getPostResult("/user/register", post);
        JSONObject data = (JSONObject) JSON.parse(result2.getString("data"));
        Assert.assertEquals(0, result2.get("code"));
        Assert.assertEquals(1012, userRepository.findAll().size());
        Assert.assertEquals("u5", data.getString("username"));
    }
}
