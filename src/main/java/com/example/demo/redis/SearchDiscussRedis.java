package com.example.demo.redis;

import com.example.demo.tmp.LastSearch;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class SearchDiscussRedis {
    @Resource
    protected RedisTemplate<String, Map<Integer, LastSearch>> redisTemplate;

    private final String name = "SEARCH_DISCUSS";

    public LastSearch getLastSearch(int userId) {
        return (LastSearch) redisTemplate.opsForHash().get(name, userId);
    }

    public void setLastSearch(int userId, int batchIndex, int index, int pageIndex) {
        redisTemplate.opsForHash().put(name, userId, new LastSearch(batchIndex, index, pageIndex));
    }

    public void deleteLastSearch(int userId) {
        redisTemplate.opsForHash().delete(name, userId);
    }
}
