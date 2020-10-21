package com.example.demo.tmp;

import java.io.Serializable;

public class UserContentLikeMapValue implements Serializable {
    private boolean isLike;
    private int change;
    private final int id;

    public UserContentLikeMapValue(boolean isLike, int change, int id) {
        this.isLike = isLike;
        this.change = change;
        this.id = id;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public int getId() {
        return id;
    }

}
