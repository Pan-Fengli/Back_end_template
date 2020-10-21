package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AdminService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdminService adminService;
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
    @WithMockUser(username = "admin", roles = "SENIOR")
    public void getDiscussNum() throws Exception {
        String teststring = new String();
        mockMvc.perform(post("/admin/discussNum")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/admin/discussNum")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"startTime\":}";//非法json字符串
        mockMvc.perform(post("/admin/discussNum")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());
        teststring = "{}";
        MvcResult result = mockMvc.perform(post("/admin/discussNum")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "没找到startTime", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        JSONObject item = new JSONObject();
        item.put("startTime", "1");
        testjson = getPostResult("/admin/discussNum", item);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "没找到endTime", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数类型错误
        item.put("endTime", "123");
        testjson = getPostResult("/admin/discussNum", item);
        Assert.assertEquals("code", 2, testjson.get("code"));
        Assert.assertEquals("message", "日期格式错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数正确
        item = new JSONObject();
        item.put("startTime", "2020-07-23");
        item.put("endTime", "2020-07-23");
        testjson = getPostResult("/admin/discussNum", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        JSONArray array = (JSONArray) testjson.get("data");
        Assert.assertEquals("length", 1, array.size());
        JSONObject data = (JSONObject) array.get(0);
        Assert.assertEquals("id", 0, data.get("num"));
        Assert.assertEquals("name", "2020-07-23", data.get("time"));

        //好像没什么业务逻辑错误。

    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", roles = "SENIOR")
    public void getRegisterNum() throws Exception {
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/admin/registerNum")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/admin/registerNum")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"startTime\":}";//非法json字符串
        mockMvc.perform(post("/admin/registerNum")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());
        teststring = "{}";
        MvcResult result = mockMvc.perform(post("/admin/registerNum")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "没找到startTime", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        JSONObject item = new JSONObject();
        item.put("startTime", "1");
        testjson = getPostResult("/admin/registerNum", item);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "没找到endTime", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数类型错误
        item.put("endTime", "123");
        testjson = getPostResult("/admin/registerNum", item);
        Assert.assertEquals("code", 2, testjson.get("code"));
        Assert.assertEquals("message", "日期格式错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数正确
        item = new JSONObject();
        item.put("startTime", "2020-07-23");
        item.put("endTime", "2020-07-23");
        testjson = getPostResult("/admin/registerNum", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        JSONArray array = (JSONArray) testjson.get("data");
        Assert.assertEquals("length", 1, array.size());
        JSONObject data = (JSONObject) array.get(0);
        Assert.assertEquals("id", 0, data.get("num"));
        Assert.assertEquals("name", "2020-07-23", data.get("time"));

        //好像没什么业务逻辑错误。

    }

    @Test
    @Transactional
    @WithUserDetails(value = "b1@qq.com")
    public void setUserState() throws Exception {
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/admin/state")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/admin/state")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"username\":}";//非法json字符串
        mockMvc.perform(post("/admin/state")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());
        //疑难杂种统统400

        //——缺少参数
        teststring = "{}";
        MvcResult result = mockMvc.perform(post("/admin/state")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
        JSONObject item = new JSONObject();
        item.put("senderId", "U1");
        testjson = getPostResult("/admin/state", item);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
//        //参数类型错误
        item.put("getterId", "U1");
        item.put("state", "U1");
        testjson = getPostResult("/admin/state", item);
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
//        //参数正确
        item = new JSONObject();
        item.put("senderId", "1011");
        item.put("getterId", "3");
        item.put("state", "1");
        testjson = getPostResult("/admin/state", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
        //从repository里面获取出数据
        User info = userRepository.getOne(1);
        Assert.assertEquals("get info不对", 0, info.getState());

        item = new JSONObject();
        item.put("senderId", "121");
        item.put("getterId", "1");
        item.put("state", "1");
        testjson = getPostResult("/admin/state", item);
        Assert.assertEquals("code", 14, testjson.get("code"));
        Assert.assertEquals("message", "用户出错", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        item = new JSONObject();
        item.put("senderId", "1011");
        item.put("getterId", "1211");
        item.put("state", "1");
        testjson = getPostResult("/admin/state", item);
        Assert.assertEquals("code", 4, testjson.get("code"));
        Assert.assertEquals("message", "对象[User]找不到", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
        item = new JSONObject();
        item.put("senderId", "1011");
        item.put("getterId", "1011");
        item.put("state", "1");
        testjson = getPostResult("/admin/state", item);
        Assert.assertEquals("code", 6, testjson.get("code"));
        Assert.assertEquals("message", "cannot set your self", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        item = new JSONObject();
        item.put("senderId", "2");
        item.put("getterId", "1");
        item.put("state", "1");
        testjson = getPostResult("/admin/state", item);
        Assert.assertEquals("code", 14, testjson.get("code"));
        Assert.assertEquals("message", "用户出错", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        item = new JSONObject();
        item.put("senderId", "1011");
        item.put("getterId", "2");
        item.put("state", "1");
        testjson = getPostResult("/admin/state", item);
        System.out.println(testjson);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "b1@qq.com")
    public void getUserList() throws Exception {
        JSONObject json_test = getGetResult("/admin/userList");
        Assert.assertEquals("code", 1, json_test.get("code"));
        Assert.assertEquals("message", "缺少参数", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/admin/userList?searchText=1x23&userId=1x23");
        Assert.assertEquals("code", 3, json_test.get("code"));
        Assert.assertEquals("message", "参数错误", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/admin/userList?searchText=1&userId=1011");
        Assert.assertEquals("code", 0, json_test.get("code"));
        Assert.assertEquals("message", "success", json_test.get("message"));
        JSONArray array = (JSONArray) json_test.get("data");
        Assert.assertEquals("length", 275, array.size());
        JSONObject data = (JSONObject) array.get(0);
        Assert.assertEquals("id", 1, data.get("id"));
        Assert.assertEquals("name", "user1", data.get("username"));

        json_test = getGetResult("/admin/userList?searchText=undefined&userId=2");
        Assert.assertEquals("code", 14, json_test.get("code"));
        Assert.assertEquals("message", "用户出错", json_test.get("message"));

        json_test = getGetResult("/admin/userList?searchText=undefined&userId=1011");
        Assert.assertEquals("code", 0, json_test.get("code"));
        Assert.assertEquals("message", "success", json_test.get("message"));

        array = (JSONArray) json_test.get("data");
        Assert.assertEquals("length", 1011, array.size());


//        String url="/admin/userList/2";
//        MvcResult result=getGetResult(url);
//        String resultContent = result.getResponse().getContentAsString();
//        JSONArray json_test = (JSONArray)JSONArray.parse (resultContent);
//        Assert.assertEquals("普通用户",0,json_test.size());
//        url="/admin/userList/16";
//        result=getGetResult(url);
//        resultContent = result.getResponse().getContentAsString();
//        json_test = (JSONArray)JSONArray.parse (resultContent);
//        Assert.assertEquals("小管理",15,json_test.size());
//        url="/admin/userList/1";
//        result=getGetResult(url);
//        resultContent = result.getResponse().getContentAsString();
//        json_test = (JSONArray)JSONArray.parse (resultContent);
//        Assert.assertEquals("大管理",16,json_test.size());

    }

    @Test
    @WithUserDetails(value = "b1@qq.com")
    public void getDiscussList() throws Exception {

        JSONObject json_test = getGetResult("/admin/discussList");
        Assert.assertEquals("code", 1, json_test.get("code"));
        Assert.assertEquals("message", "缺少参数", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/admin/discussList?startTime=1x23&endTime=1x23&userId=1x23");
        Assert.assertEquals("code", 3, json_test.get("code"));
        Assert.assertEquals("message", "参数错误", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/admin/discussList?startTime=1x23&endTime=1x23&userId=3");
        Assert.assertEquals("code", 14, json_test.get("code"));
        Assert.assertEquals("message", "用户出错", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/admin/discussList?startTime=1x23&endTime=1x23&userId=1011");
        Assert.assertEquals("code", 2, json_test.get("code"));
        Assert.assertEquals("message", "日期格式错误", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/admin/discussList?startTime=2020-07-23&endTime=2020-07-23&userId=3");
        Assert.assertEquals("code", 14, json_test.get("code"));
        Assert.assertEquals("message", "用户出错", json_test.get("message"));

        json_test = getGetResult("/admin/discussList?startTime=2020-07-13&endTime=2020-08-23&userId=1011");
        Assert.assertEquals("code", 0, json_test.get("code"));
        Assert.assertEquals("message", "success", json_test.get("message"));
        JSONArray array = (JSONArray) json_test.get("data");
        Assert.assertEquals("length", 0, array.size());

//        json_test = getGetResult("/admin/discussList?startTime=2020-07-22&endTime=2020-07-22&userId=31");
//        Assert.assertEquals("code", 4, json_test.get("code"));
//        Assert.assertEquals("message", "对象[User]找不到", json_test.get("message"));
//        Assert.assertNull("data", json_test.get("data"));
//
//        json_test = getGetResult("/admin/discussList?startTime=2020-07-22&endTime=2020-07-22&userId=1");
//        Assert.assertEquals("code", 6, json_test.get("code"));
//        Assert.assertEquals("message", "您的权限不够", json_test.get("message"));
//        Assert.assertNull("data", json_test.get("data"));

//        String url="/admin/discussList/2";
////        MvcResult result=getGetResult(url);
//        String resultContent = result.getResponse().getContentAsString();
//        JSONArray json_test = (JSONArray)JSONArray.parse (resultContent);
//        Assert.assertEquals("普通用户",0,json_test.size());
//        url="/admin/discussList/16";
//        result=getGetResult(url);
//        resultContent = result.getResponse().getContentAsString();
//        json_test = (JSONArray)JSONArray.parse (resultContent);
//        Assert.assertEquals("小管理",11,json_test.size());
//        url="/admin/discussList/1";
//        result=getGetResult(url);
//        resultContent = result.getResponse().getContentAsString();
//        json_test = (JSONArray)JSONArray.parse (resultContent);
//        Assert.assertEquals("大管理",11,json_test.size());
    }

//    @Test
//    @Transactional
//    public void sendAudit() throws Exception {
//        JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
//        jsonObject.put("sender_id", 16);
//        jsonObject.put("user_id", 2);
//        jsonObject.put("state",1);
//        jsonObject.put("content","ok");
////        MvcResult result=getPostResult("/admin/sendAudit",jsonObject);
////        Assert.assertEquals(16,adminService.getAuditList(1).get(0).getSenderItem().getId());
////        Assert.assertEquals(2,adminService.getAuditList(1).get(0).getUserItem().getId());
////        Assert.assertEquals(1,adminService.getAuditList(1).get(0).getState());
////        Assert.assertEquals("ok",adminService.getAuditList(1).get(0).getContent());
//    }
//
//    @Test
//    @Transactional
//    public void forbidUser() throws Exception {
//        JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
//        jsonObject.put("user_id", 2);
//        jsonObject.put("state",1);
////        MvcResult result=getPostResult("/admin/banuser",jsonObject);
////        Assert.assertEquals("forbid",1,adminService.getUserList(16).get(0).getState());
//    }
//
//    @Test
//    @Transactional
//    public void getAuditList() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("sender_id", 16);
//        jsonObject.put("user_id", 2);
//        jsonObject.put("state",1);
//        jsonObject.put("content","ok");
//        getPostResult("/admin/sendAudit",jsonObject);
//
////        MvcResult result=getGetResult("/admin/auditList/1");
////        String resultContent = result.getResponse().getContentAsString();
////        JSONArray json_test = (JSONArray)JSONArray.parse (resultContent);
////        Assert.assertEquals("audit",1,json_test.size());
//    }
//
//    @Ignore("not ready yet")//噢这里是我自己的测试
//    @Test
//    public void processAudits() throws Exception {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("audit_id", 12);
//        jsonObject.put("accept", false);
//        getPostResult("/admin/processAudits",jsonObject);
////        Assert.assertEquals("audit",0,adminService.getAuditList(1).size());
////        Assert.assertEquals("user",1,adminService.getUserList(1).get(0).getState());
//
//        jsonObject = new JSONObject();
//        jsonObject.put("audit_id", 13);
//        jsonObject.put("accept", true);
//        getPostResult("/admin/processAudits",jsonObject);
////        Assert.assertEquals("audit",0,adminService.getAuditList(1).size());
////        Assert.assertEquals("user",1,adminService.getUserList(1).get(0).getState());;
//
//    }


}
