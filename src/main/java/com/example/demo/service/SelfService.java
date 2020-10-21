package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.MyException;

import java.util.List;

public interface SelfService {
    SelfInfo GetOneById(int id) throws MyException;

    void EditSelf(int id, String username, String icon, String email, String gender, String hometown, String phoneNumber, String intro) throws MyException;

    List<SelfHead> FindFollow(int id) throws MyException;

    List<SelfHead> FindFollowed(int id) throws MyException;

    void CancelFollow(int id, int fid) throws MyException;

    List<DiscussItem> FindCollection(int interestId, int userId) throws MyException;

    void Follow(int id, int fid) throws MyException;

    boolean CheckFollow(int id, int fid);

    List<TagItem> findInterest(int userId) throws MyException;

    List<DiscussItem> findDiscuss(int userId, int myId) throws MyException;
}
