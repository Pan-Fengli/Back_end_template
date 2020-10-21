package com.example.demo.entity.parent;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ContentWithLike extends Content {
    private int likeNum;
    private int dislikeNum;
    public ContentWithLike(){
        super();
    }

    public ContentWithLike(int userId, String detail) {
        super(userId, detail);
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getDislikeNum() {
        return dislikeNum;
    }

    public void setDislikeNum(int dislikeNum) {
        this.dislikeNum = dislikeNum;
    }

    public void addLike() {
        likeNum++;
    }

    public void subLike() {
        likeNum--;
    }

    public void addDislike() {
        dislikeNum++;
    }

    public void subDislike() {
        dislikeNum--;
    }
}
