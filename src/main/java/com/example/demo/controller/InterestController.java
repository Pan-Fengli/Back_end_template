package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.TagItem;
import com.example.demo.dto.ResultBean;
import com.example.demo.exception.MyException;
import com.example.demo.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/interest")
public class InterestController {
    @Autowired
    private InterestService interestService;

    @PostMapping("/set")
    public ResultBean setInterest(@AuthenticationPrincipal Principal principal, @RequestBody String str) throws Exception {
        JSONObject object = JSONObject.parseObject(str);
        if (object.getInteger("userId") == null || object.getString("interestId") == null || object.getString("type") == null) {
            throw new MyException(1, "缺少参数");
        }
        int userId = object.getInteger("userId");
        int userIdS = Integer.parseInt(principal.getName());
        if (userId != userIdS) {
            throw new MyException(14, "用户出错");
        }
        int interestId = object.getInteger("interestId");
        int type = object.getInteger("type");
        interestService.changeInterest(userId, interestId, type);
        return ResultBean.success();
    }

    @GetMapping("/search")
    public ResultBean<List<TagItem>> searchInterest(@AuthenticationPrincipal Principal principal, @RequestParam String key) throws MyException {
        return ResultBean.success(interestService.searchInterest("%" + key + "%"));
    }

    @GetMapping("/getChildren")
    public ResultBean<List<TagItem>> getChildren(@AuthenticationPrincipal Principal principal, @RequestParam int id) throws MyException {
        return ResultBean.success(interestService.getChildren(id));
    }

    @GetMapping("/getOne")
    public ResultBean<TagItem> getOne(@AuthenticationPrincipal Principal principal, @RequestParam int id) throws MyException {
        return ResultBean.success(interestService.getOne(id));
    }

    @PostMapping("add")
    public ResultBean<TagItem> addOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("name") == null || map.get("id") == null)
            throw new MyException(1, "缺少参数");
        String name = map.get("name");
        int id = Integer.parseInt(map.get("id"));
        return ResultBean.success(interestService.addOne(id, name));
    }
}
