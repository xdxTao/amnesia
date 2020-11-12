package com.xdx.service.task;

import com.xdx.common.common.AjaxResult;
import com.xdx.entitys.pojo.SyTask;

import java.util.List;

public interface SyTaskService {

    /**
     * 新增任务
     */
    AjaxResult<?> add(SyTask task);

    /**
     * 获取任务数据
     */
    AjaxResult<List<SyTask>> list(Integer taskType,Integer labelId);

    /**
     * 更新任务
     */
    AjaxResult<?> update(SyTask task);

    /**
     * 更新完成
     */
    AjaxResult<?> complete(Integer taskId);

    /**
     * 任务转移
     */
    AjaxResult<?> transfer(Integer taskId);

    /**
     * 任务排序
     */
    AjaxResult<?> taskSort(List<SyTask> syTasks);
}
