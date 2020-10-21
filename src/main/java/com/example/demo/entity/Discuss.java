package com.example.demo.entity;

import com.example.demo.entity.parent.ContentWithLike;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "discuss", schema = "taoxq")
public class Discuss extends ContentWithLike {
    private String title;
    private int commentNum;
    private int starNum;
    @Transient
    private String text;

    public Discuss(int userId, String title, String detail) {
        super(userId, detail);
        this.title = title;
    }

    public Discuss() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void addCommentNum() {
        this.commentNum++;
    }

    public void subCommentNum() {
        this.commentNum--;
    }

    public int getStarNum() {
        return starNum;
    }

    public void addStarNum() {
        this.starNum++;
    }

    public void subStarNum() {
        this.starNum--;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
