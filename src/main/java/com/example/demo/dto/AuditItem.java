package com.example.demo.dto;

import com.example.demo.entity.Audit;
import com.example.demo.entity.User;

import java.util.Date;

public class AuditItem {
    private int id;
    private UserItem senderItem;
    private UserItem userItem;
    private int state;
    private Date time;
    private String content;

    public AuditItem(Audit audit, User sender, User user) {
        this.id = audit.getId();
        this.senderItem = new UserItem(sender);
        this.userItem = new UserItem(user);
        this.state = audit.getState();
        this.time = audit.getTime();
        this.content = audit.getContent();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserItem getSenderItem() {
        return senderItem;
    }

    public void setSenderItem(UserItem senderItem) {
        this.senderItem = senderItem;
    }

    public UserItem getUserItem() {
        return userItem;
    }

    public void setUserItem(UserItem userItem) {
        this.userItem = userItem;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
