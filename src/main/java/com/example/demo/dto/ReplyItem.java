package com.example.demo.dto;

import com.example.demo.dto.parent.ContentItem;
import com.example.demo.entity.Reply;
import com.example.demo.entity.User;

public class ReplyItem extends ContentItem {
    private ToReplyItem toReply;

    public ReplyItem(Reply reply, User user, Reply toReply, User toUser) {
        super(reply, user);
        this.toReply = toReply == null ? null : new ToReplyItem(toReply, toUser);
    }

    public ToReplyItem getToReply() {
        return toReply;
    }

    public void setToReply(ToReplyItem toReply) {
        this.toReply = toReply;
    }
}
