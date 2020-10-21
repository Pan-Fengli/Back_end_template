package com.example.demo.quartzTasks;

import com.example.demo.redis.RecommendDiscussRedis;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RecommendDiscussTask extends QuartzJobBean {
    @Autowired
    private RecommendDiscussRedis recommendDiscussRedis;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        recommendDiscussRedis.deleteAllRecommend();
    }
}
