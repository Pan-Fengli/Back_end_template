package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.DemoApplicationTests;
import com.example.demo.dao.DiscussDao;
import com.example.demo.dto.DiscussItem;
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
class DiscussControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper om = new ObjectMapper();

    @Autowired
    private DiscussDao discussDao;

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
    public void getOne() throws Exception {
        //异常参数是否存在检验
        JSONObject result1 = getGetResult("/discuss/one");
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        JSONObject result2 = getGetResult("/discuss/one?userId=abc&discussId=1");
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        JSONObject result3 = getGetResult("/discuss/one?userId=100&discussId=1");
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        JSONObject result4 = getGetResult("/discuss/one?userId=1&discussId=1");
        JSONObject data = (JSONObject) JSON.parse(result4.getString("data"));
        Assert.assertEquals(0, result4.get("code"));
        Assert.assertEquals(1, data.get("id"));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    public void getSome() throws Exception {
        //异常参数是否存在检验
        JSONObject result1 = getGetResult("/discuss/some");
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        JSONObject result2 = getGetResult("/discuss/some?searchText=1&pageSize=abc&pageIndex=0&userId=1&type=my");
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        JSONObject result3 = getGetResult("/discuss/some?searchText=1&pageSize=10&pageIndex=0&userId=100&type=my");
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        JSONObject result4 = getGetResult("/discuss/some?searchText=1&pageSize=10&pageIndex=0&userId=1&type=my");
//        List<DiscussItem> list = om.readValue(result4.getString("data"), new TypeReference<List<DiscussItem>>() {
//        });
        Assert.assertEquals(0, result4.get("code"));
//        Assert.assertEquals(1, list.size());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    public void addOne() throws Exception {
        //异常参数是否存在检验
        JSONObject post = new JSONObject();
        JSONObject result1 = getPostResult("/discuss/one", post);
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        post.put("userId", "abc");
        post.put("title", "讨论4");
        post.put("content", "讨论4内容");
        post.put("interests", "1,2");
        JSONObject result2 = getPostResult("/discuss/one", post);
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        post.put("userId", 100);
        JSONObject result3 = getPostResult("/discuss/one", post);
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        post.put("userId", 1);
        JSONObject result4 = getPostResult("/discuss/one", post);
        Assert.assertEquals(0, result4.get("code"));
        Assert.assertEquals("讨论4", discussDao.findById(result4.getInteger("data")).getTitle());
//        Assert.assertEquals(2, discussDao.findById(result4.getInteger("data")).getInterestList().size());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    public void likeOrDislikeOne() throws Exception {
        //异常参数是否存在检验
        JSONObject post = new JSONObject();
        JSONObject result1 = getPostResult("/discuss/like", post);
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        post.put("userId", "abc");
        post.put("discussId", 3);
        post.put("isLike", "true");
        JSONObject result2 = getPostResult("/discuss/like", post);
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        post.put("userId", 100);
        JSONObject result3 = getPostResult("/discuss/like", post);
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        post.put("userId", 1);
        JSONObject result4 = getPostResult("/discuss/like", post);
        Assert.assertEquals(0, result4.get("code"));
//        Assert.assertEquals(1, discussDao.findById(3).getLikeNum());
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    public void starOne() throws Exception {
        //异常参数是否存在检验
        JSONObject post = new JSONObject();
        JSONObject result1 = getPostResult("/discuss/star", post);
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        post.put("userId", "abc");
        post.put("discussId", 3);
        JSONObject result2 = getPostResult("/discuss/star", post);
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        post.put("userId", 100);
        JSONObject result3 = getPostResult("/discuss/star", post);
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        post.put("userId", 1);
        JSONObject result4 = getPostResult("/discuss/star", post);
        Assert.assertEquals(0, result4.get("code"));
//        Assert.assertEquals(1, discussDao.findById(3).getStaredUserList().size());
    }


    @Test
    @Transactional
    @WithUserDetails(value = "b1@qq.com")
    public void deleteOne() throws Exception {
        //异常参数是否存在检验
        JSONObject post = new JSONObject();
        JSONObject result1 = getDeleteResult("/discuss/one", post);
        Assert.assertEquals(1, result1.get("code"));
        //异常参数格式检验
        post.put("senderId", "abc");
        post.put("discussId", 3);
        JSONObject result2 = getDeleteResult("/discuss/one", post);
        Assert.assertEquals(3, result2.get("code"));
        //异常找不到对象检验
        post.put("senderId", 100);
        JSONObject result3 = getDeleteResult("/discuss/one", post);
        Assert.assertEquals(14, result3.get("code"));
        //正确性检验
        post.put("senderId", 1011);
        JSONObject result5 = getDeleteResult("/discuss/one", post);
        Assert.assertEquals(0, result5.get("code"));
//        Assert.assertEquals(2, discussDao.findAll().size());
    }
}
