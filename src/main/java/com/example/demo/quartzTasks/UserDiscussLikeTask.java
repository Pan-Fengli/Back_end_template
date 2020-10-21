package com.example.demo.quartzTasks;

import com.example.demo.redis.UserCommentLikeRedis;
import com.example.demo.redis.UserDiscussLikeRedis;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class UserDiscussLikeTask extends QuartzJobBean {
    @Autowired
    private UserDiscussLikeRedis userDiscussLikeRedis;
    @Autowired
    private UserCommentLikeRedis userCommentLikeRedis;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        userDiscussLikeRedis.saveToDB();
        userCommentLikeRedis.saveToDB();
    }
}
