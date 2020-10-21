package com.example.demo.controller;

import com.example.demo.dto.DiscussItem;
import com.example.demo.dto.NumDate;
import com.example.demo.dto.ResultBean;
import com.example.demo.dto.UserItem;
import com.example.demo.exception.MyException;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/discussNum")
    public ResultBean<List<NumDate>> getDiscussNum(@RequestBody Map<String, String> params) throws ParseException, MyException {
        if (params.get("startTime") == null) throw new MyException(1, "没找到startTime");
        if (params.get("endTime") == null) throw new MyException(1, "没找到endTime");
        return ResultBean.success(adminService.getDiscussNum(params.get("startTime"), params.get("endTime")));
    }

    @PostMapping("/registerNum")
    public ResultBean<List<NumDate>> getRegisterNum(@RequestBody Map<String, String> params) throws ParseException, MyException {
        if (params.get("startTime") == null) throw new MyException(1, "没找到startTime");
        if (params.get("endTime") == null) throw new MyException(1, "没找到endTime");
        return ResultBean.success(adminService.getRegisterNum(params.get("startTime"), params.get("endTime")));
    }

    @PostMapping(value = "/state")
    public ResultBean setUserState(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> params) throws ParseException, MyException {
        if (params.get("senderId") == null || params.get("getterId") == null || params.get("state") == null) {
            throw new MyException(1, "缺少参数");
        }
        int userId = Integer.parseInt(principal.getName()), senderId = Integer.parseInt(params.get("senderId"));
        if (userId != senderId) {
            throw new MyException(14, "用户出错");
        }
        if (params.get("getterId") == null) throw new MyException(1, "没找到endTime");
        adminService.setUserState(senderId, Integer.parseInt(params.get("getterId")), Integer.parseInt(params.get("state")));
        return ResultBean.success();
    }

    @GetMapping("/userList")
    @ResponseBody
    public ResultBean<List<UserItem>> getUserList(@AuthenticationPrincipal Principal principal, @RequestParam String searchText, @RequestParam int userId,
                                                  @RequestParam int pageSize, @RequestParam int pageIndex) throws ParseException, NumberFormatException, MyException {
        //既然都是用param，那么不用手动检查了
        int userIdS = Integer.parseInt(principal.getName());
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        return ResultBean.success(adminService.getUserList(searchText, userId, pageSize, pageIndex));
    }

    @GetMapping("/discussList")
    @ResponseBody
    public ResultBean<List<DiscussItem>> getDiscussList(@AuthenticationPrincipal Principal principal, @RequestParam String startTime, @RequestParam String endTime, @RequestParam int userId) throws ParseException, MyException {
        int userIdS = Integer.parseInt(principal.getName());
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        return ResultBean.success(adminService.getDiscussList(startTime, endTime, userId));
    }
}
