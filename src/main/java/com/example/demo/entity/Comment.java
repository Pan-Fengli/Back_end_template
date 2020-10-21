package com.example.demo.entity;

import com.example.demo.entity.parent.ContentWithLike;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "comment", schema = "taoxq")
public class Comment extends ContentWithLike {
    private int discussId;
    private int replyNum;

    public Comment(int userId, int discussId, String detail) {
        super(userId, detail);
        this.discussId = discussId;
    }

    public Comment() {
        super();
    }

    public int getDiscussId() {
        return discussId;
    }

    public void setDiscussId(int discussId) {
        this.discussId = discussId;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public void subReplyNum() {
        this.replyNum--;
    }

    public void addReplyNum() {
        this.replyNum++;
    }
}
