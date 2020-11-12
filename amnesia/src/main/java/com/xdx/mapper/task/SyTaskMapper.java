package com.xdx.mapper.task;

import com.xdx.common.common.MyBaseMapper;
import com.xdx.entitys.pojo.SyTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SyTaskMapper extends MyBaseMapper<SyTask> {

    void updateByKey(SyTask update);

    /**
     * 找到最大的排序
     */
    Integer selectMaxSort(@Param("userId") Integer userId);

    /**
     * 更新已完成的数据
     */
    void updateCompleted();

    /**
     * 移动任务
     */
    void changeTask();

}
