package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.DemoApplicationTests;
import com.example.demo.dto.DiscussItem;
import com.example.demo.dto.ReplyItem;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.ReplyRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReplyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private ReplyRepository replyRepository;

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

    private JSONObject getDeleteResult(String url, JSONObject jsonObject) throws Exception {
        String string;
        if (jsonObject == null) string = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        else string = mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return (JSONObject) JSON.parse(string);
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    public void postOne() throws Exception {
        //异常参数是否存在检验
        JSONObject post = new JSONObject();
        JSONObject result1 = getPostResult("/reply/one", post);
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        post.put("userId", "abc");
        post.put("commentId", 1);
        post.put("toUserId", 2);
        post.put("content", "1给2的回复");
        JSONObject result2 = getPostResult("/reply/one", post);
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        post.put("userId", 100);
        JSONObject result3 = getPostResult("/reply/one", post);
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        post.put("userId", 1);
        JSONObject result4 = getPostResult("/reply/one", post);
        Assert.assertEquals(0, result4.get("code"));
        Assert.assertEquals(true, replyRepository.findById(result4.getInteger("data")).isPresent());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "b1@qq.com")
    public void getSome() throws Exception {
        //异常参数是否存在检验
        JSONObject result1 = getGetResult("/reply/some");
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        JSONObject result2 = getGetResult("/reply/some?commentId=abc&pageIndex=0&pageSize=10");
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        JSONObject result3 = getGetResult("/reply/some?commentId=10110&pageIndex=0&pageSize=10");
        Assert.assertEquals(4, result3.get("code"));
        //正确性检验
        JSONObject result4 = getGetResult("/reply/some?commentId=1&pageIndex=0&pageSize=10");
        Assert.assertEquals(0, result4.get("code"));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "b1@qq.com")
    public void deleteOne() throws Exception {
        //异常参数是否存在检验
        JSONObject post = new JSONObject();
        JSONObject result1 = getDeleteResult("/reply/one", post);
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        post.put("userId", "abc");
        post.put("replyId", 1);
        JSONObject result2 = getDeleteResult("/reply/one", post);
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        post.put("userId", 100);
        JSONObject result3 = getDeleteResult("/reply/one", post);
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        Assert.assertEquals(100000, replyRepository.findAll().size());
        post.put("userId", 1011);
        JSONObject result5 = getDeleteResult("/reply/one", post);
        Assert.assertEquals(0, result5.get("code"));
        Assert.assertEquals(99999, replyRepository.findAll().size());
    }
}
