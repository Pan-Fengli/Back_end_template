package com.example.demo.controller;


import com.example.demo.dto.*;
import com.example.demo.exception.MyException;
import com.example.demo.service.SelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/self")
public class SelfController {
    @Autowired
    private SelfService userService;

    @GetMapping("/discuss")
    public ResultBean<List<DiscussItem>> DiscussList(@AuthenticationPrincipal Principal principal, @RequestParam int userId) throws MyException {
        return ResultBean.success(userService.findDiscuss(userId, Integer.parseInt(principal.getName())));
    }

    @GetMapping("/Collection")//
    public ResultBean<List<DiscussItem>> CollectionList(@AuthenticationPrincipal Principal principal, @RequestParam int interestId) throws MyException {
        return ResultBean.success(userService.FindCollection(interestId, Integer.parseInt(principal.getName())));
    }

    @GetMapping("/interest")//
    public ResultBean<List<TagItem>> InterestList(@RequestParam int userId) throws MyException {
        return ResultBean.success(userService.findInterest(userId));
    }

    @RequestMapping(value = "/Info/{id}", method = RequestMethod.GET)//
    public ResultBean<SelfInfo> GetSelfInfo(@PathVariable Integer id) throws Exception {//
        // int ID = Integer.parseInt(id);//如果不是integer会返回400，先不去管他
        SelfInfo user = userService.GetOneById(id);
        return ResultBean.success(user);
    }

    @RequestMapping(value = "/Info/{id}", method = RequestMethod.POST)//
    public ResultBean EditSelf(@AuthenticationPrincipal Principal principal, @PathVariable Integer id, @RequestBody Map<String, String> map) throws Exception {
        int userIdS = Integer.parseInt(principal.getName());
        if (id != userIdS) {
            throw new MyException(14, "用户出错");
        }
        if (map.get("username") == null || map.get("email") == null || map.get("hometown") == null || map.get("phoneNumber") == null || map.get("intro") == null) {
            throw new MyException(1, "缺少参数");
        }
        String username = map.get("username").toString();
        System.out.println(username);
        String icon = "";
        String email = map.get("email").toString();
        String gender = "";
        String hometown = map.get("hometown").toString();
        String phoneNumber = map.get("phoneNumber").toString();
        String intro = map.get("intro").toString();

        System.out.println("before");

        int ID = id;
        //接下来是根据service去找。
        userService.EditSelf(ID, username, icon, email, gender, hometown, phoneNumber, intro); //这里就是根据获取到的数据去修改
        return ResultBean.success();
    }

    @GetMapping("/follow")//
    public ResultBean<List<SelfHead>> getFollowList(@RequestParam int userId) throws Exception {
        return ResultBean.success(userService.FindFollow(userId));
    }

    @GetMapping("/followed")//
    public ResultBean<List<SelfHead>> getFollowedList(@RequestParam int userId) throws Exception {
        return ResultBean.success(userService.FindFollowed(userId));
    }

    @PostMapping("/disFollow")//
    public ResultBean cancelFollow(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws Exception {
        if (map.get("userId") == null || map.get("followId") == null) {
            throw new MyException(1, "缺少参数");
        }
        int userId = Integer.parseInt(map.get("userId"));
        int userIdS = Integer.parseInt(principal.getName());
        if (userId != userIdS) {
            throw new MyException(14, "用户出错");
        }
        userService.CancelFollow(userId, Integer.parseInt(map.get("followId")));
        return ResultBean.success();
    }

    @PostMapping("/follow")//
    public ResultBean Follow(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws Exception {
        if (map.get("userId") == null || map.get("followId") == null) {
            throw new MyException(1, "缺少参数");
        }
        int userId = Integer.parseInt(map.get("userId"));
        int userIdS = Integer.parseInt(principal.getName());
        if (userId != userIdS) {
            throw new MyException(14, "用户出错");
        }
        userService.Follow(userId, Integer.parseInt(map.get("followId")));
        return ResultBean.success();
    }

    @RequestMapping(value = "/checkfollow/{id}", method = RequestMethod.POST)//POST的唯一原因是我想传body...
    public ResultBean<CheckItem> CheckFollow(@AuthenticationPrincipal Principal principal, @PathVariable Integer id, @RequestBody Map<String, String> map) throws Exception {

        int userIdS = Integer.parseInt(principal.getName());
        if (id != userIdS) {
            throw new MyException(14, "用户出错");
        }
        int ID = id;//这个是用户的id，和取关的fid分开
        if (map.get("id") == null) {
            throw new MyException(1, "缺少参数");
        }
        int FID = Integer.parseInt(map.get("id"));
        System.out.println("id：" + ID);
        System.out.println("fid：" + FID);
        boolean result = userService.CheckFollow(ID, FID);
        return ResultBean.success(new CheckItem(result));
    }
}
