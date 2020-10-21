package com.example.demo.service;


import com.example.demo.dto.DiscussItem;
import com.example.demo.dto.NumDate;
import com.example.demo.dto.UserItem;
import com.example.demo.exception.MyException;

import java.text.ParseException;
import java.util.List;

public interface AdminService {
    List<NumDate> getRegisterNum(String startTime, String endTime) throws ParseException;

    List<NumDate> getDiscussNum(String startTime, String endTime) throws ParseException;

    void setUserState(int senderId, int getterId, int state) throws MyException;

    List<UserItem> getUserList(String searchText, int userId, int pageSize, int pageIndex) throws ParseException, MyException;

    List<DiscussItem> getDiscussList(String startTime, String endTime, int userId) throws ParseException, MyException;
}
