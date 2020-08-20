package com.xdx.service.timingtask;

import com.xdx.common.enums.TaskTypeEnum;
import com.xdx.common.enums.YesOrNoStatusEnum;
import com.xdx.common.utils.redis.CacheContext;
import com.xdx.entitys.dto.SyTask;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Configuration
public class RemoveTask {

    /**
     * 清除已经完成的任务
     *
     * 每天0点过5分清楚数据
     */
    @Scheduled(cron = "0 5 0 * * ?")
//    @Scheduled(fixedRate = 1000 * 600)
    public void remove(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()) + "  定时任务清空已完成数据");
        Set<Serializable> keys = CacheContext.getKeys();
        for (Serializable key : keys){
            // 任务数据的key是以task结尾的
            if (key.toString().endsWith("task")){
                List<SyTask> list = (List<SyTask>)CacheContext.get(key.toString());
                List<SyTask> newLists = new ArrayList<>();
                for (int i = 0;i < list.size(); i++){
                    if (list.get(i).getTaskSts() != YesOrNoStatusEnum.YES){
                        list.get(i).setTaskType(TaskTypeEnum.TOTAL);
                        newLists.add(list.get(i));
                    }
                }
                CacheContext.set(key.toString(),newLists, 60*24*365);
            }
        }
    }

}
