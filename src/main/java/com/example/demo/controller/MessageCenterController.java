package com.example.demo.controller;

import com.example.demo.dto.NotificationItem;
import com.example.demo.dto.ResultBean;
import com.example.demo.exception.MyException;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/messageCenter")
public class MessageCenterController {
    //    @Autowired
//    private MessageCenterService messageCenterService;
    @Autowired
    private NotificationService notificationService;

//    //得到关注自己的人(未读)
//    @RequestMapping("/followers/{uid}")
//    public ResultBean<List<UserUserStarItem>> getFollowers(@PathVariable Integer uid) {
//        return ResultBean.success(messageCenterService.getFollowers(uid));
//    }
//
//    //将关注自己的人全部标记为已读
//    @RequestMapping("/clearFollowers/{uid}")
//    public ResultBean clearFollowers(@PathVariable Integer uid) {
//        messageCenterService.clearFollowers(uid);
//        return ResultBean.success();
//    }
//
//    //将关注自己的人标记为已读
//    @RequestMapping("/clearFollower")
//    public ResultBean clearFollower(@RequestBody Map<String, String> params) throws MyException {
//        if (params.get("uid") == null || params.get("fuid") == null)
//            throw new MyException(1, "缺少参数");
//        int uid = Integer.parseInt(params.get("uid"));
//        int fid = Integer.parseInt(params.get("fid"));
//        messageCenterService.clearFollower(uid, fid);
//        return ResultBean.success();
//    }
//
//    //得到回复自己的回复和评论(未读)
//    @RequestMapping("/commentsAndReplies/{uid}")
//    public ResultBean<List<MessageItem>> getCommentsAndReplies(@PathVariable Integer uid) {
//        return ResultBean.success(messageCenterService.getCommentsAndReplies(uid));
//    }
//
//    //将回复自己的回复和评论全部设为已读
//    @RequestMapping("/clearCommentsAndReplies/{uid}")
//    public ResultBean clearCommentsAndReplies(@PathVariable Integer uid) {
//        messageCenterService.clearCommentsAndReplies(uid);
//        return ResultBean.success();
//    }
//
//    //将回复自己的回复或评论设为已读
//    @RequestMapping("/clearCommentOrReply")
//    public ResultBean clearCommentOrReply(@RequestBody JSONObject jsonObject) throws MyException {
//        if (jsonObject == null || jsonObject.getString("messageItemType") == null)
//            throw new MyException(1, "缺少参数");
//        MessageItemType messageItemType = MessageItemType.parseString(jsonObject.getString("messageItemType"));
//        if (messageItemType == MessageItemType.MSG_COMMENT) {
//            if (jsonObject.get("commentItem") == null)
//                throw new MyException(1, "缺少参数");
//            JSONObject jsonObject1 = (JSONObject) JSON.toJSON(jsonObject.get("commentItem"));
//            int cid = Integer.parseInt(jsonObject1.getString("id"));
//            messageCenterService.clearComment(cid);
//        } else {
//            if (jsonObject.get("replyItem") == null)
//                throw new MyException(1, "缺少参数");
//            JSONObject jsonObject1 = (JSONObject) JSON.toJSON(jsonObject.get("replyItem"));
//            int rid = Integer.parseInt(jsonObject1.getString("id"));
//            messageCenterService.clearReply(rid);
//        }
//        return ResultBean.success();
//    }
//
//    //得到自己的讨论和评论的点赞(未读)
//    @RequestMapping("/likes/{uid}")
//    public ResultBean<List<MessageItem>> getLikes(@PathVariable Integer uid) {
//        return ResultBean.success(messageCenterService.getDiscussAndCommentLike(uid));
//    }
//
//    //将自己的讨论和评论的点赞全部设为已读
//    @RequestMapping("/clearLikes/{uid}")
//    public ResultBean clearLikes(@PathVariable Integer uid) {
//        messageCenterService.clearDiscussAndCommentLike(uid);
//        return ResultBean.success();
//    }
//
//    //将自己的讨论和评论的点赞设为已读
//    @RequestMapping("/clearLike")
//    public ResultBean clearLike(@RequestBody JSONObject jsonObject) throws MyException {
//        if (jsonObject == null || jsonObject.getString("messageItemType") == null)
//            throw new MyException(1, "缺少参数");
//        MessageItemType messageItemType = MessageItemType.parseString(jsonObject.getString("messageItemType"));
//        if (messageItemType == MessageItemType.MSG_USER_DISCUSS_LIKE) {
//            if (jsonObject.get("userDiscussLikeItem") == null)
//                throw new MyException(1, "缺少参数");
//            JSONObject jsonObject1 = (JSONObject) JSON.toJSON(jsonObject.get("userDiscussLikeItem"));
//            int userDiscussLikeItemId = Integer.parseInt(jsonObject1.getString("id"));
//            messageCenterService.clearUserDiscussLike(userDiscussLikeItemId);
//        } else {
//            if (jsonObject.get("userCommentLikeItem") == null)
//                throw new MyException(1, "缺少参数");
//            JSONObject jsonObject1 = (JSONObject) JSON.toJSON(jsonObject.get("userCommentLikeItem"));
//            int userCommentLikeItemId = Integer.parseInt(jsonObject1.getString("id"));
//            messageCenterService.clearUserCommentLike(userCommentLikeItemId);
//        }
//        return ResultBean.success();
//    }

    //通知列表(未读)
    @GetMapping("/notifications")
    public ResultBean<List<NotificationItem>> getNotifications(@AuthenticationPrincipal Principal principal, @RequestParam Integer userId) throws MyException {
        int userIdS = Integer.parseInt(principal.getName());
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        List<NotificationItem> tmp = notificationService.getNotifications(userId);
        return ResultBean.success(tmp);
    }

    //将通知全部设为已读
    @GetMapping("/clearNotifications")
    public ResultBean clearNotifications(@AuthenticationPrincipal Principal principal, @RequestParam Integer userId) throws MyException {
        int userIdS = Integer.parseInt(principal.getName());
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        notificationService.clearNotifications(userId);
        return ResultBean.success();
    }

    //将通知设为已读
    @GetMapping("/clearNotification")
    public ResultBean clearNotification(@AuthenticationPrincipal Principal principal, @RequestParam int userId,
                                        @RequestParam Integer nId) throws MyException {
        int userIdS = Integer.parseInt(principal.getName());
        if (userIdS != userId) {
            throw new MyException(14, "用户出错");
        }
        notificationService.clearNotification(nId, userId);
        return ResultBean.success();
    }
}
