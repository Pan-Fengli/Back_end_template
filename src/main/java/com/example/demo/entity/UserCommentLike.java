package com.example.demo.entity;

import com.example.demo.entity.parent.UserContentLike;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_comment_like")
public class UserCommentLike extends UserContentLike {
    private int commentId;

    public UserCommentLike(int userId, int commentId, boolean isLike) {
        super(userId, isLike);
        this.commentId = commentId;
    }

    public UserCommentLike() {
        super();
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
}
