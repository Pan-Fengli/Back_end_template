//package com.example.demo.service.impl;
//
//import com.example.demo.Enum.MessageItemType;
//import com.example.demo.dao.*;
//import com.example.demo.dto.*;
//import com.example.demo.entity.*;
//import com.example.demo.service.MessageCenterService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Service
//public class MessageCenterServiceImpl implements MessageCenterService {
//    @Autowired
//    private UserDao userDao;
//    @Autowired
//    private ReplyDao replyDao;
//    @Autowired
//    private CommentDao commentDao;
//    @Autowired
//    private UserUserStarDao userUserStarDao;
//    @Autowired
//    private UserDiscussLikeDao userDiscussLikeDao;
//    @Autowired
//    private UserCommentLikeDao userCommentLikeDao;
//
//    @Override
//    public List<UserUserStarItem> getFollowers(int uid) {
//        List<UserUserStar> list=userDao.getFollowers(uid);
//        if(list==null)return new ArrayList<>();
//        List<UserUserStarItem> itemList=new ArrayList<>();
//        for(int i=0;i<list.size();++i){
//            if(!list.get(i).isRead()) {
//                UserUserStarItem userUserStarItem = new UserUserStarItem(list.get(i));
//                itemList.add(userUserStarItem);
//            }
//        }
//        return itemList;
//    }
//
//    @Override
//    public List<MessageItem> getCommentsAndReplies(Integer uid) {
//        List<MessageItem> itemList=new ArrayList<>();
//        List<Reply> replyList=replyDao.findByToUserId(uid);
//        List<Comment> commentList=commentDao.findByDiscussUserId(uid);
//
//        for(int i=0;i<replyList.size();++i){
//            if(!replyList.get(i).isRead()) {
//                MessageItem messageItem = new MessageItem();
//                messageItem.setMessageItemType(MessageItemType.MSG_REPLY);
//                messageItem.setReplyItem(new ReplyItem(replyList.get(i)));
//                itemList.add(messageItem);
//            }
//        }
//        for(int i=0;i<commentList.size();++i){
//            if(!commentList.get(i).isRead()) {
//                MessageItem messageItem = new MessageItem();
//                messageItem.setMessageItemType(MessageItemType.MSG_COMMENT);
//                messageItem.setCommentItem(new CommentItem(commentList.get(i)));
//                itemList.add(messageItem);
//            }
//        }
//        return itemList;
//    }
//    @Override
//    public List<MessageItem> getDiscussAndCommentLike(Integer uid) {
//        List<MessageItem> itemList=new ArrayList<>();
//
//        if(userDao.findById(uid)!=null) {
//            List<Discuss> discussList = userDao.findById(uid).getDiscussList();
//            for (Integer i = 0; i < discussList.size(); ++i) {
//                Discuss discuss = discussList.get(i);
//                List<UserDiscussLike> userDiscussLikeList = discuss.getLikeOrDislikeUserList();
//                for (Integer j = 0; j < userDiscussLikeList.size(); ++j) {
//                    if (!userDiscussLikeList.get(j).isRead()) {
//                        MessageItem messageItem = new MessageItem();
//                        messageItem.setMessageItemType(MessageItemType.MSG_USER_DISCUSS_LIKE);
//                        messageItem.setUserDiscussLikeItem(new UserDiscussLikeItem(userDiscussLikeList.get(j)));
//                        itemList.add(messageItem);
//                    }
//                }
//            }
//
//            List<Comment> commentList = userDao.findById(uid).getCommentList();
//            for (int i = 0; i < commentList.size(); ++i) {
//                Comment comment = commentList.get(i);
//                List<UserCommentLike> userCommentLikeList = comment.getLikeOrDislikeCommentList();
//                for (int j = 0; j < userCommentLikeList.size(); ++j) {
//                    if (!userCommentLikeList.get(j).isRead()) {
//                        MessageItem messageItem = new MessageItem();
//                        messageItem.setMessageItemType(MessageItemType.MSG_USER_COMMENT_LIKE);
//                        messageItem.setUserCommentLikeItem(new UserCommentLikeItem(userCommentLikeList.get(j)));
//                        itemList.add(messageItem);
//                    }
//                }
//            }
//        }
//
//        return itemList;
//    }
//
//    @Override
//    public void clearFollowers(Integer uid) {
//        List<UserUserStar> userUserStarList=userDao.getFollowers(uid);
//        for(int i=0;i<userUserStarList.size();++i){
//            userUserStarDao.setRead(userUserStarList.get(i));
//        }
//
//    }
//
//    @Override
//    public void clearFollower(int uid, int fid) {
//        List<UserUserStar> userUserStarList=userDao.getFollowers(uid);
//        for(int i=0;i<userUserStarList.size();++i) {
//            if(userUserStarList.get(i).getUser().getId()==fid){
//                userUserStarDao.setRead(userUserStarList.get(i));
//            }
//        }
//    }
//
//    @Override
//    public void clearCommentsAndReplies(Integer uid) {
//        List<Reply> replyList=replyDao.findByToUserId(uid);
//        List<Comment> commentList=commentDao.findByDiscussUserId(uid);
//        for(int i=0;i<replyList.size();++i){
//            if(!replyList.get(i).isRead()){
//                replyDao.setRead(replyList.get(i));
//            }
//        }
//        for(int i=0;i<commentList.size();++i){
//            if(!commentList.get(i).isRead()){
//                commentDao.setRead(commentList.get(i));
//            }
//        }
//    }
//
//    @Override
//    public void clearDiscussAndCommentLike(Integer uid) {
//        List<Discuss> discussList=userDao.findById(uid).getDiscussList();
//        for(Integer i=0;i<discussList.size();++i){
//            Discuss discuss=discussList.get(i);
//            List<UserDiscussLike> userDiscussLikeList=discuss.getLikeOrDislikeUserList();
//            for(Integer j=0;j<userDiscussLikeList.size();++j){
//                if(!userDiscussLikeList.get(j).isRead()) {
//                    userDiscussLikeDao.setRead(userDiscussLikeList.get(j));
//                }
//            }
//        }
//        List<Comment> commentList=userDao.findById(uid).getCommentList();
//        for(int i=0;i<commentList.size();++i){
//            Comment comment=commentList.get(i);
//            List<UserCommentLike> userCommentLikeList=comment.getLikeOrDislikeCommentList();
//            for(int j=0;j<userCommentLikeList.size();++j){
//                if(!userCommentLikeList.get(j).isRead()) {
//                    userCommentLikeDao.setRead(userCommentLikeList.get(j));
//                }
//            }
//        }
//    }
//
//    @Override
//    public void clearUserDiscussLike(int userDiscussLikeItemId) {
//        userDiscussLikeDao.setRead(userDiscussLikeDao.findById(userDiscussLikeItemId));
//    }
//
//    @Override
//    public void clearUserCommentLike(int userCommentLikeItemId) {
//        userCommentLikeDao.setRead(userCommentLikeDao.findById(userCommentLikeItemId));
//    }
//}
