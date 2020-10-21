package com.example.demo.controller;


import com.example.demo.dto.ResultBean;
import com.example.demo.dto.UserItem;
import com.example.demo.exception.MyException;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResultBean<UserItem> Register(@RequestBody Map<String, String> map) throws MyException {
        if (map.get("userName") == null || map.get("password") == null || map.get("email") == null || map.get("phoneNumber") == null)
            throw new MyException(1, "缺少参数");
        return ResultBean.success(userService.Register(map.get("userName"), map.get("password"), map.get("email"), map.get("phoneNumber")));
    }

    @PostMapping("/icon")
    public ResultBean SetIcon(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("icon") == null) {
            throw new MyException(1, "缺少参数");
        }
        int userId = Integer.parseInt(principal.getName());
        userService.setIcon(userId, map.get("icon"));
        return ResultBean.success();
    }
}
