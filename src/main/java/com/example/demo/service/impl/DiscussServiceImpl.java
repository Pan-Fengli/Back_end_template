package com.example.demo.service.impl;

import com.example.demo.Enum.NotificationType;
import com.example.demo.dao.DiscussDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.DiscussItem;
import com.example.demo.entity.*;
import com.example.demo.exception.MyException;
import com.example.demo.redis.RecommendDiscussRedis;
import com.example.demo.redis.SearchDiscussRedis;
import com.example.demo.redis.UserDiscussLikeRedis;
import com.example.demo.repository.*;
import com.example.demo.service.DiscussService;
import com.example.demo.service.NotificationService;
import com.example.demo.tmp.LastSearch;
import com.example.demo.tmp.LikeNAndDislikeN;
import com.example.demo.tmp.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiscussServiceImpl implements DiscussService {
    @Autowired
    private DiscussDao discussDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDiscussLikeRepository userDiscussLikeRepository;
    @Autowired
    private UserDiscussStarRepository userDiscussStarRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserDiscussLikeRedis userDiscussLikeRedis;
    @Autowired
    private RecommendDiscussRedis recommendDiscussRedis;
    @Autowired
    private DiscussInterestRepository discussInterestRepository;
    @Autowired
    private UserInterestRepository userInterestRepository;
    @Autowired
    private SearchDiscussRedis searchDiscussRedis;

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

    private List<DiscussItem> toDTOList(List<Discuss> discussList, int userId) throws MyException {
        List<DiscussItem> discussItemList = new ArrayList<>();
        for (Discuss discuss : discussList) {
            discussItemList.add(toDTO(discuss, userId));
        }
        return discussItemList;
    }

    @Override
    public int addOne(String title, String content, int userId, List<Integer> interestIdList) throws MyException {
        User user = userDao.findByIdMySQL(userId);
        if (user == null) {
            throw new MyException(4, "对象[User]找不到");
        } else if (user.getState() != 0) {
            throw new MyException(5, "你已被禁言或封号");
        }
        Discuss discuss = new Discuss(userId, title, content);
        int discussId = discussDao.save(discuss);
        for (Integer id : interestIdList) {
            Optional<Interest> interest = interestRepository.findById(id);
            if (interest.isEmpty()) {
                throw new MyException(4, "对象[Interest]找不到");
            }
            discussInterestRepository.save(new DiscussInterest(id, discussId));
        }
        return discussId;
    }

    @Override
    public void deleteOne(int senderId, int discussId) throws MyException {
        Discuss discuss = discussDao.findByIdMySQL(discussId);
        if (discuss == null) {
            throw new MyException(4, "对象[Discuss]找不到");
        }
        User sender = userDao.findByIdMySQL(senderId), user = userDao.findByIdMySQL(discuss.getUserId());
        if (sender == null) {
            throw new MyException(4, "对象[User]找不到");
        }
        if (sender.getRoot() == 0 && user.getId() != senderId || sender.getRoot() < user.getRoot()) {//普通用户不能删除别人的讨论//小管理员不能删大管理员的讨论
            throw new MyException(6, "你的权限不够");
        }
        if (senderId != user.getId()) {        //管理员删除非自己的讨论时,生成通知
            notificationService.add(NotificationType.NTF_DELETE_DISCUSS, senderId, user.getId(), discussId, null, null, null, null);
        }
        discussInterestRepository.deleteByDiscussId(discussId);
        userDiscussLikeRedis.deleteContent(discussId);
        discussDao.deleteById(discussId);
    }

    @Override
    public List<DiscussItem> getSome(String searchText, int pageSize, int pageIndex, int userId, String type) throws MyException {
        if (userDao.findByIdMySQL(userId) == null)
            throw new MyException(4, "对象[User]找不到");
        List<Discuss> discussList;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        if (!searchText.equals("")) {
            discussList = findSearch(searchText, type.equals("deep"), userId, pageSize, pageIndex);
        } else {
            searchDiscussRedis.deleteLastSearch(userId);
            switch (type) {
                case "recommend":
                    discussList = findRecommend(pageSize, pageIndex, userId);
                    break;
                case "star":
                    discussList = findStar(userId, pageable);
                    break;
                case "all":
                    discussList = findAll(pageable);
                    break;
                case "my":
                    discussList = findMy(userId, pageable);
                    break;
                default:
                    throw new MyException(3, "参数错误");
            }
        }
        return toDTOList(discussList, userId);
    }

    private List<Discuss> findAll(Pageable pageable) {
        return discussDao.findAll(pageable);
    }

    private List<Discuss> findRecommend(int pageSize, int pageIndex, int userId) {
        List<Integer> recommendIdList = recommendDiscussRedis.findRecommendByUserId(userId);
        if (recommendIdList != null) {
            List<Discuss> discussList = new ArrayList<>();
            PageHelper<Integer> helper = new PageHelper<>();
            for (int id : helper.naivePage(pageSize, pageIndex, recommendIdList)) {
                discussList.add(discussDao.findById(id));
            }
            return discussList;
        }
        List<UserInterest> userInterests = userInterestRepository.findByUserId(userId);
        List<Interest> interestList = new ArrayList<>();
        for (UserInterest userInterest : userInterests) {
            interestList.add(interestRepository.findById(userInterest.getInterestId()).get());
        }
        Set<Discuss> set = new HashSet<>();
        for (Interest interest : interestList) {
            List<DiscussInterest> discussInterestList = discussInterestRepository.findByInterestId(interest.getId());
            List<Discuss> discussList = new ArrayList<>();
            for (DiscussInterest discussInterest : discussInterestList) {
                discussList.add(discussDao.findById(discussInterest.getDiscussId()));
            }
            set.addAll(discussList);
        }
        List<Discuss> discussList = new ArrayList<>(set);
        recommendDiscussRedis.addRecommend(userId, discussList);
        PageHelper<Discuss> helper = new PageHelper<>();
        return helper.naivePage(pageSize, pageIndex, discussList);
    }

    private List<Discuss> findStar(int userId, Pageable pageable) {
        List<Discuss> discussList = new ArrayList<>();
        List<UserDiscussStar> userDiscussStars = userDiscussStarRepository.findByUserId(userId, pageable).toList();
        for (UserDiscussStar userDiscussStar : userDiscussStars) {
            discussList.add(discussDao.findById(userDiscussStar.getDiscussId()));
        }
        return discussList;
    }

    private List<Discuss> findSearch(String searchText, boolean isDeep, int userId, int pageSize, int pageIndex) {
        LastSearch lastSearch = searchDiscussRedis.getLastSearch(userId);
        int batchIndex, lastIndex;
        if (lastSearch == null || lastSearch.getPageIndex() > pageIndex) {
            lastIndex = batchIndex = 0;
        } else {
            batchIndex = lastSearch.getBatchIndex();
            lastIndex = lastSearch.getIndex();
        }
        int count = 0;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        String[] searchList = searchText.split(" ");
        List<Discuss> discussList = new ArrayList<>();
        while (count < pageSize) {
            Pageable pageable = PageRequest.of(batchIndex, LastSearch.batchNum, sort);
            List<Discuss> tmpList = discussDao.findAll(pageable);
            if (tmpList.isEmpty()) {
                batchIndex++;
                break;
            }
            for (; lastIndex < tmpList.size(); lastIndex++) {
                Discuss discuss = tmpList.get(lastIndex);
                String tmp = discuss.getTitle() + (isDeep ? discuss.getText() : "");
                boolean flag = true;
                for (String searchItem : searchList) {
                    if (searchItem.equals("")) {
                        continue;
                    }
                    if (!tmp.contains(searchItem)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    discussList.add(discuss);
                    count++;
                    if (count == pageSize) {
                        break;
                    }
                }
            }
            if (lastIndex == tmpList.size()) {
                batchIndex++;
                lastIndex = 0;
            }
        }
        searchDiscussRedis.setLastSearch(userId, batchIndex, lastIndex, pageIndex);
        return discussList;
    }

    private List<Discuss> findMy(int userId, Pageable pageable) {
        return discussDao.findByUserId(userId, pageable);
    }

    @Override
    public DiscussItem getOne(int userId, int discussId) throws MyException {
        if (discussDao.findById(discussId) == null) {
            throw new MyException(4, "对象[Discuss]找不到");
        }
        return this.toDTO(discussDao.findById(discussId), userId);
    }

    @Override
    public void likeOrDislikeOne(int userId, int discussId, boolean isLike) {
        userDiscussLikeRedis.like(userId, discussId, isLike);
    }

    @Override
    public void starOne(int userId, int discussId) throws MyException {
        UserDiscussStar userDiscussStar = userDiscussStarRepository.findOneByUserIdAndDiscussId(userId, discussId);
        User user = userDao.findById(userId);
        Discuss discuss = discussDao.findById(discussId);
        if (user == null || discuss == null) {
            throw new MyException(4, "对象[User]或[Discuss]找不到");
        }
        if (userDiscussStar == null) {
            UserDiscussStar userDiscussStar1 = new UserDiscussStar();
            userDiscussStar1.setUserId(userId);
            userDiscussStar1.setDiscussId(discussId);
            userDiscussStarRepository.saveAndFlush(userDiscussStar1);
        } else {
            userDiscussStarRepository.delete(userDiscussStar);
        }
    }
}
