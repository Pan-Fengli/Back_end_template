package com.example.demo.entity;

import com.example.demo.entity.parent.Content;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "reply", schema = "taoxq")
public class Reply extends Content {
    private int commentId;
    private int toReplyId;
    private boolean isRead;

    public Reply(int commentId, int userId, int toReplyId, String detail) {
        super(userId, detail);
        this.commentId = commentId;
        this.toReplyId = toReplyId;
    }

    public Reply() {
        super();
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getToReplyId() {
        return toReplyId;
    }

    public void setToReplyId(int toReplyId) {
        this.toReplyId = toReplyId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
