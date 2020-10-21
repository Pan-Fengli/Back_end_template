package com.example.demo.service.impl;

import com.example.demo.dao.DiscussDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.DiscussItem;
import com.example.demo.dto.SelfHead;
import com.example.demo.dto.SelfInfo;
import com.example.demo.dto.TagItem;
import com.example.demo.entity.*;
import com.example.demo.exception.MyException;
import com.example.demo.redis.UserDiscussLikeRedis;
import com.example.demo.repository.*;
import com.example.demo.service.DiscussService;
import com.example.demo.service.SelfService;
import com.example.demo.tmp.LikeNAndDislikeN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SelfServiceImpl implements SelfService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserUserStarRepository userUserStarRepository;
    @Autowired
    private UserDiscussStarRepository userDiscussStarRepository;
    @Autowired
    private UserInterestRepository userInterestRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private DiscussInterestRepository discussInterestRepository;
    @Autowired
    private DiscussRepository discussRepository;
    @Autowired
    private UserDiscussLikeRedis userDiscussLikeRedis;
    @Autowired
    private UserDiscussLikeRepository userDiscussLikeRepository;

    private DiscussItem toDTO(Discuss discuss, int userId) throws MyException {
        User user = userDao.findById(discuss.getUserId());
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        UserDiscussStar userDiscussStar = userDiscussStarRepository.findOneByUserIdAndDiscussId(userId, discuss.getId());
        List<Interest> interests = new ArrayList<>();
        List<DiscussInterest> discussInterestList = discussInterestRepository.findByDiscussId(discuss.getId());
        for (DiscussInterest discussInterest : discussInterestList) {
            interests.add(interestRepository.findById(discussInterest.getInterestId()).get());
        }
        LikeNAndDislikeN likeNAndDislikeN = userDiscussLikeRedis.getLikeN(discuss.getId());
        if (likeNAndDislikeN != null) {
            discuss.setLikeNum(likeNAndDislikeN.getLikeN());
            discuss.setDislikeNum(likeNAndDislikeN.getDislikeN());
        }
        Boolean isLike = null;
        int redisLike = userDiscussLikeRedis.findByContentIdAndUserId(discuss.getId(), userId);
        if (redisLike == 2) {
            UserDiscussLike userDiscussLike = userDiscussLikeRepository.findOneByUserIdAndDiscussId(userId, discuss.getId());
            if (userDiscussLike != null) {
                isLike = userDiscussLike.isLike();
            }
        } else if (redisLike != 3) {
            isLike = redisLike == 1;
        }
        return new DiscussItem(discuss, user, isLike != null && isLike, isLike != null && !isLike, userDiscussStar != null, interests);
    }

    @Override//
    public SelfInfo GetOneById(int id) throws MyException {
        User user = userDao.findById(id);
        //本来应该用dao的
        if (user == null) {
//            user = new User();
//            user.setId(0);//ID不能为空
//            return new SelfInfo(user);
            throw new MyException(4, "对象[User]找不到");
        }
        return new SelfInfo(user);
    }//

    @Override//
    public void EditSelf(int id, String username, String icon, String email, String gender, String hometown, String phoneNumber, String intro) throws MyException {
        //先通过id，到dao层去找到User对象
        User user = userDao.findByIdMySQL(id);
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
            //return new RtnMsg(1, "fault");
        }
        //然后根据我们的数据去修改
        user.setEmail(email);//比如这样
        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);
        user.setGender(gender);
        user.setHometown(hometown);
        user.setIntro(intro);
        userDao.saveMySQL(user);
        //然后update 还是在dao层 传入一个对象即可。
        //return new RtnMsg(0, "ok");
    }

    @Override//
    public List<SelfHead> FindFollow(int id) throws MyException {
        //return bookDao.findfollow(id);
        List<User> list = userDao.findFollow(id);//应该返回一个list，具体实现看dao
        //需要利用dao层的list来构造出一个类
        //遍历构造
        if (list == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        List<SelfHead> ulist = new ArrayList<>();
        for (User slot : list) {
            SelfInfo info = new SelfInfo(slot);
            ulist.add(new SelfHead(info));
        }

        return ulist;
    }

    @Override//
    public List<SelfHead> FindFollowed(int id) throws MyException {
        //return bookDao.findfollow(id);
        List<User> list = userDao.findFollowed(id);
        if (list == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        List<SelfHead> ulist = new ArrayList<>();
        for (User slot : list) {
            SelfInfo info = new SelfInfo(slot);
            ulist.add(new SelfHead(info));
        }

        return ulist;
    }

    @Override//
    public void CancelFollow(int id, int fid) throws MyException {
        boolean check = userDao.checkFollow(id, fid);
        if (!check)//如果没有关注，那就不谈取关
        {
            throw new MyException(10, "尚未关注");
        } else {
            userDao.cancelFollow(id, fid);//这里我认为是注定成功的(因为已经通过了上面的判断，说明一定存在这样的关系)....也可以不成功
        }
//        return new RtnMsg(1, "ok");
        //根据两个id去关系集里面找到对应的对象(如果找得到..)相当于checkfollow，然后再次进入dao层去delete。
    }

    @Override//
    public List<DiscussItem> FindCollection(int interestId, int userId) throws MyException {//草，误打误撞地，这个真的成了用户发表的帖子....那就不用改了...之后遇到bug再说吧，现在找起来太麻烦了
        List<DiscussInterest> discussInterestList = discussInterestRepository.findByInterestId(interestId);
        List<DiscussItem> dlist = new ArrayList<>();
        DiscussItem item = new DiscussItem();
        for (DiscussInterest object : discussInterestList) {
            item = toDTO(discussRepository.findById(object.getDiscussId()).get(), userId);
            dlist.add(item);
        }
        return dlist;
    }

    @Override
    public List<DiscussItem> findDiscuss(int userId, int myId) throws MyException {
        List<Discuss> discussList = discussRepository.findByUserId(userId);
        List<DiscussItem> discussItemList = new ArrayList<>();
        DiscussItem item;
        for (Discuss discuss : discussList) {
            item = toDTO(discuss, myId);
            discussItemList.add(item);
        }
        return discussItemList;
    }

    @Override//
    public void Follow(int id, int fid) throws MyException {//
        boolean check = userDao.checkFollow(id, fid);
        if (check)//如果已经关注，那就不谈再关注
        {
            throw new MyException(10, "已经关注");
        }
        UserUserStar follow = new UserUserStar();
        User user = userDao.findById(id);//这里需要根据id去找到第一个user
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        User suser = userDao.findById(fid);//这里需要根据fid去找到第二个user
        if (suser == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        follow.setUserId(id);
        follow.setStarUserId(fid);
        Date time = new Date();
        follow.setTime(time);
        follow.setRead(false);
        userUserStarRepository.save(follow);//可以直接在服务层解决这个问题（id自增？）
        //根据两个id去关系集里面找到对应的对象，然后再次进入dao层去delete。
    }

    @Override
    public boolean CheckFollow(int id, int fid) {
        //进入dao层寻找，看是否为null来决定返回值。
        return userDao.checkFollow(id, fid);
    }

    // @Override
    // public void collect(int id, int did) {
    //     UserDiscussStar follow = new UserDiscussStar();
    //     User user = userDao.findById(id);
    //     Discuss discuss = discussDao.findById(did);
    //     Date time = new Date();
    //     follow.setTime(time);
    //     follow.setUser(user);
    //     follow.setDiscuss(discuss);
    //     follow.setRead(false);//一开始是未读
    //     userDiscussStarRepository.save(follow);
    // }

    @Override//
    public List<TagItem> findInterest(int userId) throws MyException {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        List<TagItem> interests = new ArrayList<>();
        List<UserInterest> userInterests = userInterestRepository.findByUserId(userId);
        for (UserInterest userInterest : userInterests) {
            interests.add(new TagItem(interestRepository.findById(userInterest.getInterestId()).get()));
        }
        return interests;
    }
}
