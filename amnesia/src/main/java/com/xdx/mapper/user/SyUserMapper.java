package com.xdx.mapper.user;

import com.xdx.common.common.MyBaseMapper;
import com.xdx.entitys.pojo.SyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SyUserMapper extends MyBaseMapper<SyUser> {

    int chechMsg(@Param("userId") String userId,@Param("msgId") String msgId);
}
