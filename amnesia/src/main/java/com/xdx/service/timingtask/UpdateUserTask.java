package com.xdx.service.timingtask;

import com.xdx.common.utils.redis.CacheContext;
import com.xdx.entitys.pojo.SyUser;
import com.xdx.mapper.user.SyUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * 定时更新用户信息,存入redis
 */
@Configuration
public class UpdateUserTask {


    @Autowired
    private SyUserMapper userMapper;

    @Scheduled(cron = "0 5 0 * * ?")
    public void updateUser(){
        List<SyUser> syUsers = userMapper.selectList(new SyUser());
        for (SyUser user : syUsers){
            CacheContext.set(user.getWxOpenId(),user, 60*26);
        }
    }
}
