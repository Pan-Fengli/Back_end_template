package com.example.demo.dto;

import java.util.Date;
import java.util.Objects;

public class UserDiscussLikeItem {
    private int id;
    private UserItem userItem;
    private DiscussItem discussItem;
    private Date time;
    private boolean isLike;
    private boolean isRead;

    public UserItem getUserItem() { return userItem; }

    public void setUserItem(UserItem userItem) { this.userItem = userItem; }

    public DiscussItem getDiscussItem() { return discussItem; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDiscussLikeItem that = (UserDiscussLikeItem) o;
        return Objects.equals(userItem.getId(), that.userItem.getId()) &&
                Objects.equals(discussItem.getId(), that.discussItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userItem.getId(), discussItem.getId());
    }

    public void setDiscussItem(DiscussItem discussItem) { this.discussItem = discussItem; }

    public Date getTime() { return time; }

    public void setTime(Date time) { this.time = time; }

    public boolean isLike() { return isLike; }

    public void setLike(boolean like) { isLike = like; }

    public boolean isRead() { return isRead; }

    public void setRead(boolean read) { isRead = read; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
