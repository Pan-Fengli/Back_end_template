package com.example.demo.service.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.dto.UserItem;
import com.example.demo.entity.User;
import com.example.demo.exception.MyException;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserItem Register(String userName, String password, String email, String phoneNumber) throws MyException {
        User user = new User();
        if (userDao.findByEmail(email) != null) {
            throw new MyException(12, "注册邮箱重复");
        }
        BCryptPasswordEncoder bcp = new BCryptPasswordEncoder();
        String encode = bcp.encode(password);
        user.setUsername(userName);
        user.setPassword(encode);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setState(0);
        userDao.save(user);
        return new UserItem(user);
    }

    @Override
    public void setIcon(int userId, String icon) throws MyException {
        User user = userDao.findByIdMySQL(userId);
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        userDao.setIcon(userId, icon);
    }
}
