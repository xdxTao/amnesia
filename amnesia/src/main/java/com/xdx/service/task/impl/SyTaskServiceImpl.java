package com.xdx.service.task.impl;

import com.xdx.common.common.AjaxResult;
import com.xdx.common.common.MyCommonService;
import com.xdx.common.enums.TaskTypeEnum;
import com.xdx.common.enums.YesOrNoStatusEnum;
import com.xdx.entitys.pojo.SyTask;
import com.xdx.mapper.task.SyTaskMapper;
import com.xdx.service.task.SyTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SyTaskServiceImpl extends MyCommonService implements SyTaskService {

    private final static Logger logger = LoggerFactory.getLogger(SyTaskServiceImpl.class);


    @Autowired
    private SyTaskMapper taskMapper;

    @Override
    public AjaxResult<?> add(SyTask task) {

        task.setTaskDel(YesOrNoStatusEnum.NO);
        task.setCreateTime(new Date());
        task.setTaskSts(YesOrNoStatusEnum.NO);
        task.setUserId(getCurUser().getUserId());
        task.setTaskType(TaskTypeEnum.TODAY);
        Integer maxSort = taskMapper.selectMaxSort(task.getUserId());
        if (maxSort == null){
            maxSort = 0;
        }
        task.setTaskSort(maxSort + 1);
        taskMapper.insert(task);
        return AjaxResult.success("新增成功");
    }

    /**
     * 获取任务数据
     */
    @Override
    public AjaxResult<List<SyTask>> list(Integer taskType,Integer labelId) {
        SyTask select = new SyTask();
        select.setUserId(getCurUser().getUserId());
        select.setTaskType(taskType == 0 ? TaskTypeEnum.TODAY : TaskTypeEnum.TOTAL);
        select.setLabelId(labelId);
        select.setTaskDel(YesOrNoStatusEnum.NO);
        select.setTaskSts(YesOrNoStatusEnum.NO);
        List<SyTask> list1 = taskMapper.selectList(select,"asc","task_sort");
        select.setTaskSts(YesOrNoStatusEnum.YES);
        List<SyTask> list2 =  taskMapper.selectList(select,"asc","task_sort");
        list1.addAll(list2);
        return AjaxResult.success(list1);
    }

    /**
     * 更新任务
     */
    @Override
    public AjaxResult<?> update(SyTask task){
        taskMapper.updateById(task);
        return AjaxResult.success("更新成功");
    }

    /**
     * 任务完成
     */
    @Override
    public AjaxResult<?> complete(Integer taskId){
        SyTask update = new SyTask();
        update.setTaskId(taskId);
        update.setTaskSts(YesOrNoStatusEnum.YES);
        taskMapper.updateById(update);
        return AjaxResult.success("任务完成");
    }

    /**
     * 任务转移
     */
    @Override
    public AjaxResult<?> transfer(Integer taskId) {

        SyTask syTask = taskMapper.selectById(taskId);
        if (syTask.getTaskType() == TaskTypeEnum.TODAY){
            syTask.setTaskType(TaskTypeEnum.TOTAL);
        }else{
            syTask.setTaskType(TaskTypeEnum.TODAY);
        }
        taskMapper.updateById(syTask);
        return AjaxResult.success("操作完成");
    }

    /**
     * 任务排序
     */
    @Override
    public AjaxResult<?> taskSort(List<SyTask> syTasks) {
        SyTask update = new SyTask();
        int i = 1;
        for (SyTask item : syTasks){
            update.setTaskId(item.getTaskId());
            update.setTaskSort(i++);
            taskMapper.updateById(update);
        }
        return AjaxResult.success("操作完成");
    }
}
