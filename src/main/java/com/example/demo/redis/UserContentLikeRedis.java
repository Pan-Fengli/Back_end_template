package com.example.demo.redis;

import com.example.demo.entity.parent.UserContentLike;
import com.example.demo.tmp.LikeNAndDislikeN;
import com.example.demo.tmp.UserContentLikeMap;
import com.example.demo.tmp.UserContentLikeMapValue;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UserContentLikeRedis<T extends UserContentLike> {
    @Resource
    protected RedisTemplate<String, Map<Integer, UserContentLikeMap>> redisTemplate;

    protected abstract String getName();

    protected abstract List<T> getByContentId(int contentId);

    protected abstract T newUserContentLike(int userId, int contentId, boolean isLike);

    protected abstract void deleteByIdList(List<Integer> deleteIdList);

    protected abstract void addUserContentLike(List<T> userContentLikeList);

    protected abstract void saveContentLikeNum(int id, int likeN, int dislikeN);

    public void like(int userId, int contentId, boolean status) {
        String name = getName();
        UserContentLikeMap userContentLikeMap = (UserContentLikeMap) redisTemplate.opsForHash().get(name, contentId);
        if (userContentLikeMap == null) {
            Map<Integer, UserContentLikeMapValue> map = new HashMap<>();
            int likeN = 0, dislikeN = 0;
            List<T> tList = getByContentId(contentId);
            for (T t : tList) {
                map.put(t.getUserId(), new UserContentLikeMapValue(t.isLike(), 0, t.getId()));
                if (t.isLike()) {
                    likeN++;
                } else {
                    dislikeN++;
                }
            }
            UserContentLikeMap userContentLikeMap1 = new UserContentLikeMap(likeN, dislikeN, map);
            redisTemplate.opsForHash().put(name, contentId, userContentLikeMap1);
            userContentLikeMap = userContentLikeMap1;
        }
        Map<Integer, UserContentLikeMapValue> map = userContentLikeMap.getMap();
        UserContentLikeMapValue userContentLikeMapValue = map.get(userId);
        if (userContentLikeMapValue == null) {
            map.put(userId, new UserContentLikeMapValue(status, 1, 0));
            if (status) {
                userContentLikeMap.addLikeCount();
            } else {
                userContentLikeMap.addDislikeCount();
            }
        } else if (userContentLikeMapValue.isLike() == status) {
            int change = userContentLikeMapValue.getChange();
            if (change == 1) {
                map.remove(userId);
            } else if (change == 0 || change == 3) {
                userContentLikeMapValue.setChange(2);
            } else {
                userContentLikeMapValue.setChange(0);
            }
            if (status) {
                userContentLikeMap.subLikeCount();
            } else {
                userContentLikeMap.subDislikeCount();
            }
        } else {
            int change = userContentLikeMapValue.getChange();
            if (change == 1) {
                map.remove(userId);
                map.put(userId, new UserContentLikeMapValue(status, 1, 0));
            } else if (change == 0 || change == 2) {
                userContentLikeMapValue.setChange(3);
                userContentLikeMapValue.setLike(status);
            } else {
                userContentLikeMapValue.setChange(0);
                userContentLikeMapValue.setLike(status);
            }
            if (status) {
                userContentLikeMap.subDislikeCount();
                userContentLikeMap.addLikeCount();
            } else {
                userContentLikeMap.subLikeCount();
                userContentLikeMap.addDislikeCount();
            }
        }
        redisTemplate.opsForHash().put(name, contentId, userContentLikeMap);
    }

    public LikeNAndDislikeN getLikeN(int contentId) {
        String name = getName();
        UserContentLikeMap map = (UserContentLikeMap) redisTemplate.opsForHash().get(name, contentId);
        if (map == null) {
            return null;
        }
        return new LikeNAndDislikeN(map.getLikeNum(), map.getDislikeNum());
    }

    public void deleteContent(int contentId) {
        redisTemplate.opsForHash().delete(getName(), contentId);
    }

    public int findByContentIdAndUserId(int contentId, int userId) {
        UserContentLikeMap map = (UserContentLikeMap) redisTemplate.opsForHash().get(getName(), contentId);
        if (map == null) {
            return 2;
        }
        UserContentLikeMapValue userContentLikeMapValue = map.getMap().get(userId);
        if (userContentLikeMapValue == null || userContentLikeMapValue.getChange() == 2) {
            return 3;
        }
        return userContentLikeMapValue.isLike() ? 1 : 0;
    }

    public void saveToDB() {
        System.out.println("flush");
        String name = getName();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(name, ScanOptions.NONE);
        List<T> addList = new ArrayList<>();
        List<Integer> deleteIdList = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            int contentId = (int) entry.getKey();
            UserContentLikeMap userContentLikeMap = (UserContentLikeMap) entry.getValue();
            for (Map.Entry<Integer, UserContentLikeMapValue> entry1 : userContentLikeMap.getMap().entrySet()) {
                int userId = entry1.getKey();
                UserContentLikeMapValue userContentLikeMapValue = entry1.getValue();
                int change = userContentLikeMapValue.getChange();
                if (change == 1 || change == 3) {
                    addList.add(newUserContentLike(userId, contentId, userContentLikeMapValue.isLike()));
                }
                if (change == 2 || change == 3) {
                    deleteIdList.add(userContentLikeMapValue.getId());
                }
            }
            saveContentLikeNum(contentId, userContentLikeMap.getLikeNum(), userContentLikeMap.getDislikeNum());
            redisTemplate.opsForHash().delete(name, contentId);
        }
        deleteByIdList(deleteIdList);
        addUserContentLike(addList);
    }
}
