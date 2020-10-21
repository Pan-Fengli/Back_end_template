package com.example.demo.entity;

import com.example.demo.entity.parent.UserContentLike;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_discuss_like")
public class UserDiscussLike extends UserContentLike {
    private int discussId;

    public UserDiscussLike(int userId, int discussId, boolean isLike) {
        super(userId, isLike);
        this.discussId = discussId;
    }

    public UserDiscussLike() {
        super();
    }

    public int getDiscussId() {
        return discussId;
    }

    public void setDiscussId(int discussId) {
        this.discussId = discussId;
    }
}
