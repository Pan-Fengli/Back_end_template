package com.example.demo.redis;

import com.example.demo.entity.Discuss;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RecommendDiscussRedis {
    @Resource
    protected RedisTemplate<String, Map<Integer, List<Integer>>> redisTemplate;

    private final String name = "RECOMMEND_DISCUSS";

    public void addRecommend(int userId, List<Discuss> discussList) {
        System.out.println("add recommend of user id: " + userId);
        List<Integer> idList = new ArrayList<>();
        for (Discuss discuss : discussList) {
            idList.add(discuss.getId());
        }
        redisTemplate.opsForHash().put(name, userId, idList);
    }

    public List<Integer> findRecommendByUserId(int userId) {
        return (List<Integer>) redisTemplate.opsForHash().get(name, userId);
    }

    public void deleteAllRecommend() {
        System.out.println("delete recommend");
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(name, ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            int userId = (int) entry.getKey();
            redisTemplate.opsForHash().delete(name, userId);
        }
    }
}
