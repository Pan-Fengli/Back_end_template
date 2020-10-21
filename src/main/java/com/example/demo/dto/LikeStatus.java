package com.example.demo.dto;

public class LikeStatus {
    private Boolean hasLike;
    private Boolean hasDislike;
    private Boolean hasStar;

    public Boolean getHasLike() {
        return hasLike;
    }

    public void setHasLike(Boolean hasLike) {
        this.hasLike = hasLike;
    }

    public Boolean getHasDislike() {
        return hasDislike;
    }

    public void setHasDislike(Boolean hasDislike) {
        this.hasDislike = hasDislike;
    }

    public Boolean getHasStar() {
        return hasStar;
    }

    public void setHasStar(Boolean hasStar) {
        this.hasStar = hasStar;
    }
}
