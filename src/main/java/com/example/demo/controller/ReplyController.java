package com.example.demo.controller;

import com.example.demo.dto.ReplyItem;
import com.example.demo.dto.ResultBean;
import com.example.demo.exception.MyException;
import com.example.demo.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/reply")
public class ReplyController {
    @Autowired
    private ReplyService replyService;

    @PostMapping("/one")
    public ResultBean<Integer> postOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("userId") == null || map.get("commentId") == null || map.get("content") == null) {
            throw new MyException(1, "缺少参数");
        }
        int userId = Integer.parseInt(map.get("userId"));
        int userIdS = Integer.parseInt(principal.getName());
        if (userId != userIdS) {
            throw new MyException(14, "用户出错");
        }
        return ResultBean.success(replyService.addOne(userId, Integer.parseInt(map.get("commentId")),
                map.get("toReplyId") == null ? 0 : Integer.parseInt(map.get("toReplyId")), map.get("content")));
    }

    @GetMapping("/some")
    public ResultBean<List<ReplyItem>> getSome(@RequestParam("commentId") int commentId, @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize) throws MyException {
        return ResultBean.success(replyService.getSome(commentId, pageIndex, pageSize));
    }

    @DeleteMapping("/one")
    public ResultBean deleteOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("userId") == null || map.get("replyId") == null) {
            throw new MyException(1, "缺少参数");
        }
        int userId = Integer.parseInt(map.get("userId"));
        int userIdS = Integer.parseInt(principal.getName());
        if (userId != userIdS) {
            throw new MyException(14, "用户出错");
        }
        replyService.deleteOne(userId, Integer.parseInt(map.get("replyId")));
        return ResultBean.success();
    }
}
