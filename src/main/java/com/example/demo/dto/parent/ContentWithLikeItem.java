package com.example.demo.dto.parent;

import com.example.demo.entity.User;
import com.example.demo.entity.parent.ContentWithLike;

public class ContentWithLikeItem extends ContentItem {
    private int likeNum;
    private int dislikeNum;
    private boolean hasLike;
    private boolean hasDislike;

    public ContentWithLikeItem() {
        super();
    }

    public ContentWithLikeItem(ContentWithLike contentWithLike, User user, boolean hasLike, boolean hasDislike) {
        super(contentWithLike, user);
        this.likeNum = contentWithLike.getLikeNum();
        this.dislikeNum = contentWithLike.getDislikeNum();
        this.hasLike = hasLike;
        this.hasDislike = hasDislike;
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

    public boolean isHasLike() {
        return hasLike;
    }

    public void setHasLike(boolean hasLike) {
        this.hasLike = hasLike;
    }

    public boolean isHasDislike() {
        return hasDislike;
    }

    public void setHasDislike(boolean hasDislike) {
        this.hasDislike = hasDislike;
    }
}
