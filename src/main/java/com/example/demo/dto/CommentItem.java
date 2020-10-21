package com.example.demo.dto;

import com.example.demo.dto.parent.ContentWithLikeItem;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;

public class CommentItem extends ContentWithLikeItem {
    private int replyNum;

    public CommentItem(Comment comment, User user, boolean hasLike, boolean hasDisLike) {
        super(comment, user, hasLike, hasDisLike);
        this.replyNum = comment.getReplyNum();
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }
}
