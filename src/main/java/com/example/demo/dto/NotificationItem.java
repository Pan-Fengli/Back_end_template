package com.example.demo.dto;


import com.example.demo.entity.Notification;
import com.example.demo.entity.User;

import java.util.Date;

public class NotificationItem {
    private int id;
    private UserItem sender;
    private UserItem getter;
    private String content;
    private boolean isRead;
    private Date time;

    public NotificationItem(Notification notification, User sender, User getter){
        id=notification.getId();
        this.sender=new UserItem(sender);
        this.getter=new UserItem(getter);
        content=notification.getContent();
        isRead=notification.isRead();
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public UserItem getSender() { return sender; }

    public void setSender(UserItem sender) { this.sender = sender; }

    public UserItem getGetter() { return getter; }

    public void setGetter(UserItem getter) { this.getter = getter; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public Date getTime() { return time; }

    public void setTime(Date time) { this.time = time; }
}
