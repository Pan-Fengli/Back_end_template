package com.example.demo.tmp;

import java.io.Serializable;
import java.util.Map;

public class UserContentLikeMap implements Serializable {
    private int likeNum;
    private int dislikeNum;
    private Map<Integer, UserContentLikeMapValue> map;

    public UserContentLikeMap(int likeN, int dislikeN, Map<Integer, UserContentLikeMapValue> m) {
        likeNum = likeN;
        dislikeNum = dislikeN;
        map = m;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public int getDislikeNum() {
        return dislikeNum;
    }

    public Map<Integer, UserContentLikeMapValue> getMap() {
        return map;
    }

    public void setMap(Map<Integer, UserContentLikeMapValue> map) {
        this.map = map;
    }

    public void addLikeCount() {
        this.likeNum++;
    }

    public void subLikeCount() {
        this.likeNum--;
    }

    public void addDislikeCount() {
        this.dislikeNum++;
    }

    public void subDislikeCount() {
        this.dislikeNum--;
    }
}
