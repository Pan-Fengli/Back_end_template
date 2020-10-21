package com.example.demo.dto;

import com.example.demo.entity.User;

import java.util.Date;

public class UserItem {
    private int id;
    private String username;
    private String icon;
    private int root;
    private int state;
    private Date time;
    private boolean isWaiting;

    public UserItem(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.icon = user.getIcon();
        this.root = user.getRoot();
        this.state = user.getState();
        this.time = user.getTime();
        this.isWaiting = user.isWaiting();
    }

    public UserItem() {
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getRoot() {
        return root;
    }

    public void setRoot(int root) {
        this.root = root;
    }
}
