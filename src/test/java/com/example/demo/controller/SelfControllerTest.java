package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.DemoApplicationTests;
import com.example.demo.controller.SelfController;
import com.example.demo.dto.RtnMsg;
import com.example.demo.dto.SelfInfo;
import com.example.demo.entity.User;
import com.example.demo.entity.UserUserStar;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserUserStarRepository;
import com.example.demo.service.SelfService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class SelfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUserStarRepository userUserStarRepository;

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
    public void getSelfInfo() throws Exception {
        mockMvc.perform(
                get("/self/Info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(
                get("/self/Info/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        //缺少参数

        JSONObject json_test = getGetResult("/self/Info/1x013");

        Assert.assertEquals("code", 3, json_test.get("code"));
        Assert.assertEquals("message", "参数错误", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));
        //参数错误（类型不对）

        json_test = getGetResult("/self/Info/1011");

        Assert.assertEquals("code", 0, json_test.get("code"));
        Assert.assertEquals("message", "success", json_test.get("message"));
        JSONObject data = (JSONObject) json_test.get("data");
        Assert.assertEquals("id", 1011, data.get("id"));


        json_test = getGetResult("/self/Info/10011");

        Assert.assertEquals("code", 4, json_test.get("code"));
        Assert.assertEquals("message", "对象[User]找不到", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));
//        //业务逻辑
    }

    @Test
    @WithUserDetails(value = "b1@qq.com")
    public void collectionList() throws Exception {

        JSONObject json_test = getGetResult("/self/Collection");
        Assert.assertEquals("code", 1, json_test.get("code"));
        Assert.assertEquals("message", "缺少参数", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/self/Collection?interestId=1x23");
        Assert.assertEquals("code", 3, json_test.get("code"));
        Assert.assertEquals("message", "参数错误", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/self/Collection?interestId=1");
        System.out.println(json_test);
        Assert.assertEquals("code",0,json_test.get("code"));
        Assert.assertEquals("message","success",json_test.get("message"));
        JSONArray array = (JSONArray) json_test.get("data");
        Assert.assertEquals("length",13,array.size());

    }

    @Test
    @WithUserDetails(value = "b1@qq.com")
    public void interestList() throws Exception {
//        mockMvc.perform(
//                get("/self/Info")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is4xxClientError());
        JSONObject json_test = getGetResult("/self/interest");
        Assert.assertEquals("code", 1, json_test.get("code"));
        Assert.assertEquals("message", "缺少参数", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/self/interest?userId=1x23");
        Assert.assertEquals("code", 3, json_test.get("code"));
        Assert.assertEquals("message", "参数错误", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/self/interest?userId=1011");
        Assert.assertEquals("code", 0, json_test.get("code"));
        Assert.assertEquals("message", "success", json_test.get("message"));
        JSONArray array = (JSONArray) json_test.get("data");
        Assert.assertEquals("length", 1, array.size());
        JSONObject data = (JSONObject) array.get(0);
        Assert.assertEquals("id", 75, data.get("id"));
        Assert.assertEquals("name", "interest75", data.get("name"));

        json_test = getGetResult("/self/interest?userId=1123");
        Assert.assertEquals("code", 4, json_test.get("code"));
        Assert.assertEquals("message", "对象[User]找不到", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    public void editSelf() throws Exception {
        //先测一些特殊情况（post body特有的）
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/self/Info/100")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/self/Info/100")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"username\":}";//非法json字符串
        mockMvc.perform(post("/self/Info/100")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());
        //疑难杂种统统400

        //——缺少参数
        teststring = "{}";
        MvcResult result = mockMvc.perform(post("/self/Info/100")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 14, testjson.get("code"));


        JSONObject item = new JSONObject();
        item.put("username", "U1");
        testjson = getPostResult("/self/Info/1", item);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数类型错误
        testjson = getPostResult("/self/Info/1xo", item);
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
        //参数正确
        item = new JSONObject();
        item.put("username", "黑人");
        item.put("icon", "");//暂时icon是“”
        item.put("email", "test@taoxq.com");
        item.put("gender", "");//暂时也是“”
        item.put("hometown", "M78");
        item.put("phoneNumber", "10086");
        item.put("intro", "test");
        testjson = getPostResult("/self/Info/1", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
//
        //从repository里面获取出数据
        User info = userRepository.getOne(1);
        Assert.assertEquals("get info不对", 1, info.getId());
        Assert.assertEquals("get name不对", "黑人", info.getUsername());
        Assert.assertEquals("get icon", "", info.getIcon());
        Assert.assertEquals("get email", "test@taoxq.com", info.getEmail());
        Assert.assertEquals("get gender", "", info.getGender());
        Assert.assertEquals("get home", "M78", info.getHometown());
        Assert.assertEquals("get phone", "10086", info.getPhoneNumber());
        Assert.assertEquals("get intro", "test", info.getIntro());
//
//        //业务逻辑
//        testjson=getPostResult("/self/Info/100",item);
//        Assert.assertEquals("code",4,testjson.get("code"));
//        Assert.assertEquals("message","对象[User]找不到",testjson.get("message"));
//        Assert.assertNull("data", testjson.get("data"));
    }

    @Test
    @WithUserDetails(value = "b1@qq.com")
    public void followList() throws Exception {
        JSONObject json_test = getGetResult("/self/follow");
        Assert.assertEquals("code", 1, json_test.get("code"));
        Assert.assertEquals("message", "缺少参数", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));

        json_test = getGetResult("/self/follow?userId=1x23");
        Assert.assertEquals("code", 3, json_test.get("code"));
        Assert.assertEquals("message", "参数错误", json_test.get("message"));
        Assert.assertNull("data", json_test.get("data"));
//
        json_test = getGetResult("/self/follow?userId=1");
        Assert.assertEquals("code", 0, json_test.get("code"));
        Assert.assertEquals("message", "success", json_test.get("message"));
        JSONArray array = (JSONArray) json_test.get("data");
        Assert.assertEquals("length", 4, array.size());

    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    public void cancelFollow() throws Exception {
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/self/disFollow")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/self/disFollow")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"userId\":}";//非法json字符串
        mockMvc.perform(post("/self/disFollow")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());

        //——缺少参数
        teststring = "{}";
        MvcResult result = mockMvc.perform(post("/self/disFollow")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        JSONObject item = new JSONObject();
        item.put("userId", 1);
        testjson = getPostResult("/self/disFollow", item);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数类型错误
        item = new JSONObject();
        item.put("userId", "1xsdsa");
        item.put("followId", "1xsdsa");
        testjson = getPostResult("/self/disFollow", item);
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数正确
        item = new JSONObject();
        item.put("userId", "1");//1或者“1”都无所谓
        item.put("followId", "743");//暂时icon是“”
        testjson = getPostResult("/self/disFollow", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //从repository里面获取出数据
        UserUserStar check = userUserStarRepository.findOneUserUserStarByStarUserIdAndUserId(2, 1);
        Assert.assertNull("找不到该对象", check);

        //业务逻辑
        item = new JSONObject();
        item.put("userId", "1");//1或者“1”都无所谓
        item.put("followId", "2");//暂时icon是
        testjson = getPostResult("/self/disFollow", item);
        Assert.assertEquals("code", 10, testjson.get("code"));
        Assert.assertEquals("message", "尚未关注", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));
    }

    @Test
    @Transactional
    @WithUserDetails(value = "1@qq.com")
    public void follow() throws Exception {
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/self/follow")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/self/follow")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"userId\":}";//非法json字符串
        mockMvc.perform(post("/self/follow")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());

        //——缺少参数
        teststring = "{}";
        MvcResult result = mockMvc.perform(post("/self/follow")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        JSONObject item = new JSONObject();
        item.put("userId", 1);
        testjson = getPostResult("/self/follow", item);
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数类型错误
        item = new JSONObject();
        item.put("userId", "1xsdsa");
        item.put("followId", "1xsdsa");
        testjson = getPostResult("/self/follow", item);
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        item = new JSONObject();
        item.put("userId", "2");//1或者“1”都无所谓
        item.put("followId", "3");
        testjson = getPostResult("/self/follow", item);
        Assert.assertEquals("code", 14, testjson.get("code"));

        //参数正确
        item = new JSONObject();
        item.put("userId", "1");//1或者“1”都无所谓
        item.put("followId", "3");
        testjson = getPostResult("/self/follow", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //从repository里面获取出数据
        UserUserStar check = userUserStarRepository.findOneUserUserStarByStarUserIdAndUserId(3, 1);
        Assert.assertNotNull("找到了该对象", check);
//
        //业务逻辑
        item = new JSONObject();
        item.put("userId", "1");//1或者“1”都无所谓
        item.put("followId", "5");//暂时icon是
        testjson = getPostResult("/self/follow", item);
        Assert.assertEquals("code", 0, testjson.get("code"));

//        item = new JSONObject();
//        item.put("userId", "5");//1或者“1”都无所谓
//        item.put("followId", "2");//暂时icon是
//        testjson = getPostResult("/self/follow", item);
//        Assert.assertEquals("code", 4, testjson.get("code"));
//        Assert.assertEquals("message", "对象[User]找不到", testjson.get("message"));
//        Assert.assertNull("data", testjson.get("data"));

    }

    @Test
    @WithUserDetails(value = "1@qq.com")
    public void checkFollow() throws Exception {
        String teststring = "";//这种程度的parse没问题
        mockMvc.perform(post("/self/checkfollow/100")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());//传入一个“”，400 badrequest
        mockMvc.perform(post("/self/checkfollow/100")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        teststring = "{\"id\":}";//非法json字符串
        mockMvc.perform(post("/self/checkfollow/100")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().is4xxClientError());
        //疑难杂种统统400

        //——缺少参数
        teststring = "{}";
        MvcResult result = mockMvc.perform(post("/self/checkfollow/1")
                .contentType(MediaType.APPLICATION_JSON).content(teststring))
                .andExpect(status().isOk()).andReturn();
        result.getResponse().setCharacterEncoding("UTF-8");
        JSONObject testjson = JSONObject.parseObject(result.getResponse().getContentAsString());
        Assert.assertEquals("code", 1, testjson.get("code"));
        Assert.assertEquals("message", "缺少参数", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));


        mockMvc.perform(post("/self/checkfollow")
                .contentType(MediaType.APPLICATION_JSON))//相当于什么都没有传(第一种情况)和上面一样是400
                .andExpect(status().is4xxClientError());

        //参数类型错误
        JSONObject item = new JSONObject();
        item.put("id", "U1");
        testjson = getPostResult("/self/checkfollow/1xo", item);
        Assert.assertEquals("code", 3, testjson.get("code"));
        Assert.assertEquals("message", "参数错误", testjson.get("message"));
        Assert.assertNull("data", testjson.get("data"));

        //参数正确
        item = new JSONObject();
        item.put("id", "5");
        testjson = getPostResult("/self/checkfollow/1", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        JSONObject data = (JSONObject) testjson.get("data");
        Assert.assertEquals("result", false, data.get("result"));

        item = new JSONObject();
        item.put("id", "2");
        testjson = getPostResult("/self/checkfollow/1", item);
        Assert.assertEquals("code", 0, testjson.get("code"));
        Assert.assertEquals("message", "success", testjson.get("message"));
        data = (JSONObject) testjson.get("data");
        Assert.assertEquals("result", false, data.get("result"));

        item = new JSONObject();
        item.put("id", "2");
        testjson = getPostResult("/self/checkfollow/3", item);
        Assert.assertEquals("code", 14, testjson.get("code"));


        //业务逻辑
    }

}
