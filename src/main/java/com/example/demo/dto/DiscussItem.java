package com.example.demo.dto;

import com.example.demo.dto.parent.ContentWithLikeItem;
import com.example.demo.entity.Discuss;
import com.example.demo.entity.Interest;
import com.example.demo.entity.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiscussItem extends ContentWithLikeItem implements Serializable {
    private String title;
    private List<TagItem> tagItemList;
    private int commentNum;
    private int starNum;
    private boolean hasStar;

    public DiscussItem() {
        super();
    }

    public DiscussItem(Discuss discuss, User user, boolean hasLike, boolean hasDislike, boolean hasStar, List<Interest> interestList) {
        super(discuss, user, hasLike, hasDislike);
        this.title = discuss.getTitle();
        this.commentNum = discuss.getCommentNum();
        this.starNum = discuss.getStarNum();
        this.hasStar = hasStar;
        tagItemList = new ArrayList<>();
        for (Interest interest : interestList) {
            tagItemList.add(new TagItem(interest));
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TagItem> getTagItemList() {
        return tagItemList;
    }

    public void setTagItemList(List<TagItem> tagItemList) {
        this.tagItemList = tagItemList;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getStarNum() {
        return starNum;
    }

    public void setStarNum(int starNum) {
        this.starNum = starNum;
    }

    public boolean isHasStar() {
        return hasStar;
    }

    public void setHasStar(boolean hasStar) {
        this.hasStar = hasStar;
    }
}
