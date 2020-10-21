package com.example.demo.controller;

import com.example.demo.dto.AuditItem;
import com.example.demo.dto.ResultBean;
import com.example.demo.exception.MyException;
import com.example.demo.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/audit")
public class AuditController {
    @Autowired
    private AuditService auditService;

    @GetMapping("/list")
    public ResultBean<List<AuditItem>> getList(@AuthenticationPrincipal Principal principal, @RequestParam int userId) throws MyException {
        int userIdS = Integer.parseInt(principal.getName());
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        return ResultBean.success(auditService.getUndone(userId));
    }

    @PostMapping("/one")
    public ResultBean postOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("senderId") == null || map.get("userId") == null || map.get("state") == null || map.get("content") == null) {
            throw new MyException(1, "缺少参数");
        }
        int senderId = Integer.parseInt(map.get("senderId"));
        int userIdS = Integer.parseInt(principal.getName());
        if (senderId != userIdS) {
            throw new MyException(14, "用户出错");
        }
        auditService.postOne(senderId, Integer.parseInt(map.get("userId")),
                Integer.parseInt(map.get("state")), map.get("content"));
        return ResultBean.success();
    }

    @PostMapping("/process")
    public ResultBean process(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("senderId") == null || map.get("auditId") == null || map.get("accept") == null || map.get("content") == null) {
            throw new MyException(1, "缺少参数");
        }
        int senderId = Integer.parseInt(map.get("senderId"));
        int userIdS = Integer.parseInt(principal.getName());
        if (senderId != userIdS) {
            throw new MyException(14, "用户出错");
        }
        auditService.process(Integer.parseInt(map.get("senderId")), Integer.parseInt(map.get("auditId")), map.get("accept").equals("true"), map.get("content"));
        return ResultBean.success();
    }
}
