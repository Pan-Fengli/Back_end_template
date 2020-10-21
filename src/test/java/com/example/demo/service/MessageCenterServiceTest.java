package com.example.demo.service;

import com.example.demo.DemoApplicationTests;
import com.example.demo.Enum.MessageItemType;
import com.example.demo.dto.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageCenterServiceTest  {
//    @Autowired
//    private MessageCenterService messageCenterService;

    @Test
    @Transactional
    public void getFollowers() {
//        List<UserUserStarItem> list = new ArrayList<>();
//        UserUserStarItem item1 = new UserUserStarItem(1, 3, 1, false);
//        UserUserStarItem item2 = new UserUserStarItem(2, 2, 1, false);
//        list.add(item1);
//        list.add(item2);
//        Assert.assertEquals(list, messageCenterService.getFollowers(1));
    }

    @Test
    @Transactional
    public void getCommentsAndReplies() {
//        List<MessageItem> list = new ArrayList<>();
//
//        ReplyItem replyItem = new ReplyItem();
//        replyItem.setId(1);
//        MessageItem messageItem = new MessageItem(MessageItemType.MSG_REPLY, null, replyItem, null, null);
//        ReplyItem replyItem1 = new ReplyItem();
//        replyItem1.setId(2);
//        MessageItem messageItem1 = new MessageItem(MessageItemType.MSG_REPLY, null, replyItem1, null, null);
//        list.add(messageItem);
//        list.add(messageItem1);
//
//        Assert.assertEquals(list, messageCenterService.getCommentsAndReplies(2));
    }

    @Test
    @Transactional
    public void getDiscussAndCommentLike() {
//        List<MessageItem> list = new ArrayList<>();
//
//        UserItem userItem = new UserItem();
//        userItem.setId(2);
//        DiscussItem discussItem = new DiscussItem();
//        discussItem.setId(1);
//        UserDiscussLikeItem userDiscussLikeItem = new UserDiscussLikeItem(userItem,discussItem);
//        MessageItem messageItem = new MessageItem(MessageItemType.MSG_USER_DISCUSS_LIKE, null, null, userDiscussLikeItem, null);
//
//        UserItem userItem1 = new UserItem();
//        userItem1.setId(3);
//        CommentItem commentItem=new CommentItem();
//        commentItem.setId(3);
//        UserCommentLikeItem userCommentLikeItem=new UserCommentLikeItem(userItem1,commentItem);
//        MessageItem messageItem1=new MessageItem(MessageItemType.MSG_USER_COMMENT_LIKE,null,null,null,userCommentLikeItem);
//
//        list.add(messageItem);
//        list.add(messageItem1);
//
//        Assert.assertEquals(list, messageCenterService.getDiscussAndCommentLike(1));
    }

    @Test
    @Transactional
    public void clearFollowers() {
//        Assert.assertEquals(2, messageCenterService.getFollowers(1).size());
//        messageCenterService.clearFollowers(1);
//        Assert.assertEquals(0, messageCenterService.getFollowers(1).size());
    }

    @Test
    @Transactional
    public void clearFollower() {
//        Assert.assertEquals(2, messageCenterService.getFollowers(1).size());
//        messageCenterService.clearFollower(1,2);
//        Assert.assertEquals(1, messageCenterService.getFollowers(1).size());
    }

    @Test
    @Transactional
    public void clearCommentsAndReplies() {
//        Assert.assertEquals(4,messageCenterService.getCommentsAndReplies(1).size());
//        messageCenterService.clearCommentsAndReplies(1);
//        Assert.assertEquals(0,messageCenterService.getCommentsAndReplies(1).size());
    }

    @Test
    @Transactional
    public void clearComment() {
//        Assert.assertEquals(4,messageCenterService.getCommentsAndReplies(1).size());
//        messageCenterService.clearComment(1);
//        Assert.assertEquals(3,messageCenterService.getCommentsAndReplies(1).size());
//        messageCenterService.clearComment(2);
//        Assert.assertEquals(2,messageCenterService.getCommentsAndReplies(1).size());
    }

    @Test
    @Transactional
    public void clearReply() {
//        Assert.assertEquals(4,messageCenterService.getCommentsAndReplies(1).size());
//        messageCenterService.clearReply(4);
//        Assert.assertEquals(3,messageCenterService.getCommentsAndReplies(1).size());
    }

    @Test
    @Transactional
    public void clearDiscussAndCommentLike() {
//        Assert.assertEquals(2,messageCenterService.getDiscussAndCommentLike(1).size());
//        messageCenterService.clearDiscussAndCommentLike(1);
//        Assert.assertEquals(0,messageCenterService.getDiscussAndCommentLike(1).size());
    }

    @Test
    @Transactional
    public void clearUserDiscussLike() {
//        Assert.assertEquals(2,messageCenterService.getDiscussAndCommentLike(1).size());
//        messageCenterService.clearUserDiscussLike(1);
//        Assert.assertEquals(1,messageCenterService.getDiscussAndCommentLike(1).size());
    }

    @Test
    @Transactional
    public void clearUserCommentLike() {
//        Assert.assertEquals(2,messageCenterService.getDiscussAndCommentLike(1).size());
//        messageCenterService.clearUserCommentLike(1);
//        Assert.assertEquals(1,messageCenterService.getDiscussAndCommentLike(1).size());
    }
}
