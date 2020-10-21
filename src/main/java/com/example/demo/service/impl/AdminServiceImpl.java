package com.example.demo.service.impl;


import com.example.demo.Enum.NotificationType;
import com.example.demo.dao.DiscussDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.DiscussItem;
import com.example.demo.dto.NumDate;
import com.example.demo.dto.UserItem;
import com.example.demo.entity.Discuss;
import com.example.demo.entity.DiscussInterest;
import com.example.demo.entity.Interest;
import com.example.demo.entity.User;
import com.example.demo.exception.MyException;
import com.example.demo.repository.DiscussInterestRepository;
import com.example.demo.repository.InterestRepository;
import com.example.demo.service.AdminService;
import com.example.demo.service.NotificationService;
import com.example.demo.tmp.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"adminCache"})
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private DiscussDao discussDao;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private DiscussInterestRepository discussInterestRepository;
    @Autowired
    private InterestRepository interestRepository;

    private List<NumDate> initNumDate(Date startDate, Date endDate) throws ParseException {
        int len = (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 3600 / 24) + 1;//问题只可能处在这里——格式不对
        List<NumDate> numDateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        for (int i = 0; i < len; i++) {
            numDateList.add(new NumDate(0, calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return numDateList;
    }

    @Override
    public List<NumDate> getDiscussNum(String startTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startTime), endDate = sdf.parse(endTime);
        List<NumDate> numDateList = initNumDate(startDate, endDate);
        List<Discuss> discussList = discussDao.findAll();
        for (Discuss discuss : discussList) {
            if (discuss.getTime().before(startDate) ||
                    (discuss.getTime().getTime() - endDate.getTime()) >= 24 * 1000 * 3600) {
                continue;
            }
            int index = (int) ((discuss.getTime().getTime() - startDate.getTime()) / 1000 / 3600 / 24);
            numDateList.get(index).setNum(numDateList.get(index).getNum() + 1);
        }
        return numDateList;
    }

    @Override
    public List<NumDate> getRegisterNum(String startTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startTime), endDate = sdf.parse(endTime);
        List<NumDate> numDateList = initNumDate(startDate, endDate);
        List<User> userList = userDao.findAll();
        for (User user : userList) {
            if (user.getTime().before(startDate)
                    || user.getTime().getTime() - endDate.getTime() >= 24 * 1000 * 3600) {
                continue;
            }
            int index = (int) ((user.getTime().getTime() - startDate.getTime()) / 1000 / 3600 / 24);
            numDateList.get(index).setNum(numDateList.get(index).getNum() + 1);
        }
        return numDateList;
    }

    @Override
    public List<UserItem> getUserList(String searchText, int userId, int pageSize, int pageIndex) throws ParseException, MyException {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        User user1 = userDao.findById(userId);
        if (user1 == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        int userRoot = user1.getRoot();
        if (userRoot == 0) {
            throw new MyException(6, "您的权限不够");
        }
        List<User> userList;
        if (searchText.equals("undefined")) {
            userList = userDao.findAll(pageable);
        } else {
            List<User> userList1 = new ArrayList<>();
            PageHelper<User> helper = new PageHelper<>();
            List<User> userAll = userDao.findAll();
            for (User user : userAll) {
                if (user.getUsername().contains(searchText)) {
                    userList1.add(user);
                }
            }
            userList = helper.naivePage(pageSize, pageIndex, userList1);
        }
        List<UserItem> userItems = new ArrayList<>();
        for (User user : userList) {
            userItems.add(new UserItem(user));
        }
        return userItems;
    }

    @Override
    public List<DiscussItem> getDiscussList(String startTime, String endTime, int userId) throws ParseException, MyException {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        if (user.getRoot() == 0) {
//            return new ArrayList<>();
            throw new MyException(6, "您的权限不够");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startTime), endDate = sdf.parse(endTime);
        List<Discuss> discussList = discussDao.findAll();
        List<DiscussItem> discussItemList = new ArrayList<>();
        System.out.println(endDate);
        for (Discuss discuss : discussList) {
            if (discuss.getTime().before(startDate)
                    || discuss.getTime().getTime() - endDate.getTime() >= 24 * 1000 * 3600) {
                continue;
            }
            User user1 = userDao.findById(discuss.getUserId());
            List<DiscussInterest> discussInterestList = discussInterestRepository.findByDiscussId(discuss.getId());
            List<Interest> interests = new ArrayList<>();
            for (DiscussInterest discussInterest : discussInterestList) {
                interests.add(interestRepository.findById(discussInterest.getInterestId()).get());
            }
            discussItemList.add(new DiscussItem(discuss, user1, false, false, false, interests));
        }
        return discussItemList;
    }

    @Override
    public void setUserState(int senderId, int getterId, int state) throws MyException {
        if (senderId == getterId) {
//            return new RtnMsg<>(0, "cannot set your self");
            throw new MyException(6, "cannot set your self");
        }
        User sender = userDao.findById(senderId);
        User getter = userDao.findById(getterId);
        if (sender == null || getter == null) {
//            return new RtnMsg<>(0, "no such user");
            throw new MyException(4, "对象[User]找不到");
        }
        if (sender.getRoot() == 2) {//只有大管理员可以直接设置用户状态
            if (state != getter.getState()) {//自己设置自己状态或者新设置的状态与用户原状态相同时不会发送通知
                NotificationType type;
                if (state == 0) type = NotificationType.NTF_RETURN_NORMAL;
                else if (state == 1) type = NotificationType.NTF_BAN_SPEECH;
                else type = NotificationType.NTF_BAN_ACCOUNT;

                notificationService.add(type, senderId, getterId, null, null, null, null, null);
            }
            getter.setState(state);
            userDao.save(getter);
//            return new RtnMsg<>(1, "success");
        } else {
//            return new RtnMsg<>(0, "root error");
            throw new MyException(6, "权限错误");
        }
    }
}
