package com.example.demo.controller;

import com.example.demo.dto.DiscussItem;
import com.example.demo.dto.ResultBean;
import com.example.demo.exception.MyException;
import com.example.demo.service.DiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/discuss")
@EnableAutoConfiguration
public class DiscussController {
    @Autowired
    private DiscussService discussService;

    @GetMapping("/one")
    @ResponseBody
    public ResultBean<DiscussItem> getOne(@AuthenticationPrincipal Principal principal, @RequestParam int userId, @RequestParam int discussId) throws MyException {
        System.out.println(principal.getName());
        int userIdS = Integer.parseInt(principal.getName());
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        return ResultBean.success(discussService.getOne(userId, discussId));
    }

    @GetMapping("/some")
    @ResponseBody
    public ResultBean<List<DiscussItem>> getSome(@AuthenticationPrincipal Principal principal, @RequestParam String searchText, @RequestParam int pageSize, @RequestParam int pageIndex,
                                                 @RequestParam int userId, @RequestParam String type) throws MyException {
        int userIdS = Integer.parseInt(principal.getName());
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        return ResultBean.success(discussService.getSome(searchText, pageSize, pageIndex, userId, type));
    }

    @PostMapping("/one")
    @ResponseBody
    public ResultBean<Integer> addOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("title") == null || map.get("content") == null || map.get("userId") == null || map.get("interests") == null)
            throw new MyException(1, "缺少参数");
        int userIdS = Integer.parseInt(principal.getName());
        int userId = Integer.parseInt(map.get("userId"));
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        String[] stringList = map.get("interests").split(",");
        List<Integer> interestIdList = new ArrayList<>();
        for (String str : stringList) {
            interestIdList.add(Integer.parseInt(str));
        }
        return ResultBean.success(discussService.addOne(map.get("title"), map.get("content"), userId, interestIdList));
    }

    @PostMapping("/like")
    @ResponseBody
    public ResultBean likeOrDislikeOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("userId") == null || map.get("discussId") == null || map.get("isLike") == null)
            throw new MyException(1, "缺少参数");
        int userIdS = Integer.parseInt(principal.getName());
        int userId = Integer.parseInt(map.get("userId"));
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        discussService.likeOrDislikeOne(userId, Integer.parseInt(map.get("discussId")), map.get("isLike").equals("true"));
        return ResultBean.success();
    }

    @PostMapping("/star")
    @ResponseBody
    public ResultBean starOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("userId") == null || map.get("discussId") == null)
            throw new MyException(1, "缺少参数");
        int userIdS = Integer.parseInt(principal.getName());
        int userId = Integer.parseInt(map.get("userId"));
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        discussService.starOne(userId, Integer.parseInt(map.get("discussId")));
        return ResultBean.success();
    }

    @DeleteMapping("/one")
    @ResponseBody
    public ResultBean deleteOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("senderId") == null || map.get("discussId") == null)
            throw new MyException(1, "缺少参数");
        int userIdS = Integer.parseInt(principal.getName());
        int userId = Integer.parseInt(map.get("senderId"));
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        discussService.deleteOne(userId, Integer.parseInt(map.get("discussId")));
        return ResultBean.success();
    }
}
