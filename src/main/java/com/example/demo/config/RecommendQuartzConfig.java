package com.example.demo.config;

import com.example.demo.quartzTasks.RecommendDiscussTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecommendQuartzConfig {

    private static final String RECOMMEND_TASK_IDENTITY = "RecommendTaskQuartz";

    @Bean
    public JobDetail quartzDetail2(){
        return JobBuilder.newJob(RecommendDiscussTask.class).withIdentity(RECOMMEND_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger quartzTrigger2(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(60)  //设置时间周期
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail2())
                .withIdentity(RECOMMEND_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}

