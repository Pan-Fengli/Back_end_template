package com.example.demo.dto;

import com.example.demo.Enum.MessageItemType;

import java.util.Objects;

public class MessageItem {
    private MessageItemType messageItemType;
    private CommentItem commentItem;
    private ReplyItem replyItem;
    private UserDiscussLikeItem userDiscussLikeItem;
    private UserCommentLikeItem userCommentLikeItem;

    public MessageItem(MessageItemType messageItemType, CommentItem commentItem, ReplyItem replyItem, UserDiscussLikeItem userDiscussLikeItem, UserCommentLikeItem userCommentLikeItem) {
        this.messageItemType = messageItemType;
        this.commentItem = commentItem;
        this.replyItem = replyItem;
        this.userDiscussLikeItem = userDiscussLikeItem;
        this.userCommentLikeItem = userCommentLikeItem;
    }

    public MessageItem() {

    }

    public MessageItemType getMessageItemType() {
        return messageItemType;
    }

    public void setMessageItemType(MessageItemType messageItemType) {
        this.messageItemType = messageItemType;
    }

    public CommentItem getCommentItem() { return commentItem; }

    public void setCommentItem(CommentItem commentItem) { this.commentItem = commentItem; }

    public ReplyItem getReplyItem() { return replyItem; }

    public void setReplyItem(ReplyItem replyItem) { this.replyItem = replyItem; }


    public UserDiscussLikeItem getUserDiscussLikeItem() {
        return userDiscussLikeItem;
    }

    public void setUserDiscussLikeItem(UserDiscussLikeItem userDiscussLikeItem) {
        this.userDiscussLikeItem = userDiscussLikeItem;
    }

    public UserCommentLikeItem getUserCommentLikeItem() {
        return userCommentLikeItem;
    }

    public void setUserCommentLikeItem(UserCommentLikeItem userCommentLikeItem) {
        this.userCommentLikeItem = userCommentLikeItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageItem that = (MessageItem) o;
        return messageItemType == that.messageItemType &&
                Objects.equals(commentItem, that.commentItem) &&
                Objects.equals(replyItem, that.replyItem) &&
                Objects.equals(userDiscussLikeItem, that.userDiscussLikeItem) &&
                Objects.equals(userCommentLikeItem, that.userCommentLikeItem);
    }

    @Override
    public int hashCode() {
        if(messageItemType==MessageItemType.MSG_REPLY){
            return Objects.hash(messageItemType,replyItem.getId());
        }else if(messageItemType==MessageItemType.MSG_COMMENT){
            return Objects.hash(messageItemType,commentItem.getId());
        }if(messageItemType==MessageItemType.MSG_USER_DISCUSS_LIKE){
            return Objects.hash(messageItemType,userDiscussLikeItem.getId());
        }else{
            return Objects.hash(messageItemType,userCommentLikeItem.getId());
        }
    }
}
