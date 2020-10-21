package com.example.demo.dto.parent;

import com.example.demo.dto.UserItem;
import com.example.demo.entity.User;
import com.example.demo.entity.parent.Content;

import java.util.Date;

public class ContentItem {
    private int id;
    private String content;
    private UserItem user;
    private Date time;

    public ContentItem() {

    }

    public ContentItem(Content content, User user) {
        this.id = content.getId();
        this.content = content.getDetail();
        this.time = content.getTime();
        this.user = new UserItem(user);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserItem getUser() {
        return user;
    }

    public void setUser(UserItem user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
