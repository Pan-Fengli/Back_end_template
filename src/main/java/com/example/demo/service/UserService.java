package com.example.demo.service;

import com.example.demo.dto.UserItem;
import com.example.demo.exception.MyException;

public interface UserService {
    UserItem Register(String userName, String password, String email, String phoneNumber) throws MyException;

    void setIcon(int userId, String icon) throws MyException;
}
