package com.xdx.timingtask;

import com.xdx.mapper.task.SyTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@Async
@EnableAsync
public class ChangeTask {

    @Autowired
    private SyTaskMapper taskMapper;

    /**
     * 把今日没有完成的任务移动到任务总览里面去
     *
     * 每天0点过1分执行
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void change(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()) + "  定时移动任务数据");
        taskMapper.changeTask();
    }
}
