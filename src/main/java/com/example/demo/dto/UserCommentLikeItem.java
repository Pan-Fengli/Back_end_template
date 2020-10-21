package com.example.demo.dto;

import java.util.Date;
import java.util.Objects;

public class UserCommentLikeItem {
    private int id;
    private UserItem userItem;
    private CommentItem commentItem;
    private Date time;
    private boolean isLike;
    private boolean isRead;

    public UserItem getUserItem() { return userItem; }

    public void setUserItem(UserItem userItem) { this.userItem = userItem; }

    public CommentItem getCommentItem() { return commentItem; }

    public void setCommentItem(CommentItem commentItem) { this.commentItem = commentItem; }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCommentLikeItem that = (UserCommentLikeItem) o;
        return Objects.equals(userItem.getId(), that.userItem.getId()) &&
                Objects.equals(commentItem.getId(), that.commentItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userItem.getId(), commentItem.getId());
    }
}
