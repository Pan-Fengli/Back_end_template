package com.example.demo.service.impl;

import com.example.demo.dto.TagItem;
import com.example.demo.entity.Interest;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInterest;
import com.example.demo.exception.MyException;
import com.example.demo.repository.InterestRepository;
import com.example.demo.repository.UserInterestRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InterestServiceImpl implements InterestService {

    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInterestRepository userInterestRepository;


    private List<TagItem> toDTOList(List<Interest> interestList) throws MyException {
        List<TagItem> tagItemList = new ArrayList<>();
        if (interestList == null) {
            throw new MyException(4, "对象[Interest]找不到");//实际上这里是覆盖不到的....因为库里面肯定都是有的
        }
        for (Interest interest : interestList) {
            Optional<Interest> parentInterest = interestRepository.findById(interest.getParentId());
            tagItemList.add(new TagItem(interest, parentInterest.get().getName()));
        }
        return tagItemList;
    }

    private TagItem toDTO(Interest interest) throws MyException {
        Optional<Interest> parentInterest = interestRepository.findById(interest.getParentId());
        TagItem tagItem = new TagItem(interest, parentInterest.get().getName());
        return tagItem;
    }

    @Override
    public List<TagItem> getAll() throws MyException {
        List<Interest> interestList = interestRepository.findAll();
        System.out.println("after findall");
        return toDTOList(interestList);
    }

    @Override
    public List<TagItem> searchInterest(String key) throws MyException {
        List<Interest> interestList = interestRepository.findByNameLike(key);
        return toDTOList(interestList);
    }

    @Override
    public List<TagItem> getChildren(int id) throws MyException {
        List<Interest> interestList = interestRepository.findByParentId(id);
        return toDTOList(interestList);
    }

    @Override
    public TagItem getOne(int id) throws MyException {
        Optional<Interest> interest = interestRepository.findById(id);
        Interest one = interest.get();
        return toDTO(one);
    }

    @Override
    public TagItem addOne(int parentId, String name) throws MyException {
        Optional<Interest> interests = interestRepository.findByName(name);
        if (!(interests.isEmpty())) {
            throw new MyException(4, "已经存在该兴趣");
        }
        Optional<Interest> interest = interestRepository.findById(parentId);
        if (interest.isEmpty()) {
            throw new MyException(-1, "父兴趣不存在");
        }
        Interest one = new Interest();
        one.setName(name);
        one.setParentId(parentId);
        interestRepository.saveAndFlush(one);
        Optional<Interest> newOne = interestRepository.findByName(name);
        return toDTO(newOne.get());
    }

    @Override
    public void changeInterest(int userId, int interestId, int type) throws MyException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new MyException(4, "对象[User]找不到");
        }
        switch (type) {
            case 0: {
                //添加兴趣
//                List<Interest> interests = user.get().getInterestList();
//                Optional<Interest> item = interestRepository.findById(interestId);
//                if (item.isEmpty()) {
//                    throw new MyException(4, "对象[Interest]找不到");
//                }
//                interests.add(item.get());
//                user.get().setInterestList(interests);
                UserInterest userInterest = new UserInterest();
                userInterest.setUserId(userId);
                userInterest.setInterestId(interestId);
                userInterestRepository.save(userInterest);
                break;
            }
            case 1: {
                //取消兴趣
                userInterestRepository.deleteByUserIdAndInterestId(userId, interestId);
                break;
            }
        }
    }
}
