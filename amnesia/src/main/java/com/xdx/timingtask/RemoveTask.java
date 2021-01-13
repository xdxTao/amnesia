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
public class RemoveTask {

    /**
     * 清除已经完成的任务
     *
     * 每天0点过5分清除数据
     */
    @Autowired
    private SyTaskMapper taskMapper;
    @Scheduled(cron = "0 5 0 * * ?")
//    @Scheduled(fixedRate = 1000 * 600)
    public void remove(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()) + "  定时任务清空已完成数据");
        taskMapper.updateCompleted();
    }
}
