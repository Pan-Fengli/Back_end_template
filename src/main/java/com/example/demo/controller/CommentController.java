package com.example.demo.controller;

import com.example.demo.dto.CommentItem;
import com.example.demo.dto.ResultBean;
import com.example.demo.exception.MyException;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/some")
    public ResultBean<List<CommentItem>> getSome(@AuthenticationPrincipal Principal principal, @RequestParam Integer userId, @RequestParam Integer discussId,
                                                 @RequestParam Integer pageIndex, @RequestParam Integer pageSize) throws MyException {
        int userIdS = Integer.parseInt(principal.getName());
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        return ResultBean.success(commentService.getSome(userId, discussId, pageIndex, pageSize));
    }

    @PostMapping("/like")
    public ResultBean likeOrDislikeOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("userId") == null || map.get("commentId") == null || map.get("isLike") == null)
            throw new MyException(1, "缺少参数");
        int userIdS = Integer.parseInt(principal.getName());
        int userId = Integer.parseInt(map.get("userId"));
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        commentService.likeOrDislikeOne(userId, Integer.parseInt(map.get("commentId")), map.get("isLike").equals("true"));
        return ResultBean.success();
    }

    @PostMapping("/one")
    public ResultBean<Integer> postOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("userId") == null || map.get("discussId") == null || map.get("content") == null)
            throw new MyException(1, "缺少参数");
        int userIdS = Integer.parseInt(principal.getName());
        int userId = Integer.parseInt(map.get("userId"));
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        return ResultBean.success(commentService.addOne(Integer.parseInt(map.get("userId")), Integer.parseInt(map.get("discussId")), map.get("content")));
    }

    @DeleteMapping("/one")
    public ResultBean deleteOne(@AuthenticationPrincipal Principal principal, @RequestBody Map<String, String> map) throws MyException {
        if (map.get("senderId") == null || map.get("commentId") == null)
            throw new MyException(1, "缺少参数");
        int userIdS = Integer.parseInt(principal.getName());
        int userId = Integer.parseInt(map.get("senderId"));
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        commentService.deleteOne(Integer.parseInt(map.get("senderId")), Integer.parseInt(map.get("commentId")));
        return ResultBean.success();
    }
}
