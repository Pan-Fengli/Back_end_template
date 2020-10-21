package com.example.demo.dto;

import com.example.demo.dto.parent.ContentItem;
import com.example.demo.entity.Reply;
import com.example.demo.entity.User;

public class ToReplyItem extends ContentItem {
    public ToReplyItem(Reply reply, User user) {
        super(reply, user);
    }
}
