package com.xdx.service.task.impl;

import com.xdx.common.common.AjaxResult;
import com.xdx.common.common.MyCommonService;
import com.xdx.common.enums.TaskTypeEnum;
import com.xdx.common.enums.YesOrNoStatusEnum;
import com.xdx.common.utils.UUIDUtils;
import com.xdx.common.utils.redis.CacheContext;
import com.xdx.entitys.dto.SyTask;
import com.xdx.service.task.SyTaskService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SyTaskServiceImpl extends MyCommonService implements SyTaskService {

    @Override
    public AjaxResult<?> add(SyTask task) {
        String key = getToken() + "task";
        // 1、取出之前的数据
        List<SyTask> list = getTaskList();

        // 2、
        if (list == null){
            list = new ArrayList<>();
        }

        // 3、新增数据
        task.setTaskId(UUIDUtils.getUUID())
                .setTaskSts(YesOrNoStatusEnum.NO)
                .setTaskType(TaskTypeEnum.TODAY);
        list.add(task);

        // 4、数据存入redis
        CacheContext.set(key, list, 60*24*365);
        return AjaxResult.success("新增成功");
    }

    /**
     * 获取任务数据
     */
    @Override
    public AjaxResult<List<SyTask>> list(Integer taskType,Integer labelId) {
        List<SyTask> taskList = getTaskList();
        if (taskList == null){
            return AjaxResult.success();
        }
        List<SyTask> lits1 = new ArrayList<>();
        List<SyTask> lits2 = new ArrayList<>();
        for (SyTask item : taskList){
            if (item.getTaskType().getCode().equals(taskType)){
                if (item.getTaskSts() == YesOrNoStatusEnum.YES){
                    lits2.add(item);
                }else {
                    lits1.add(item);
                }
            }
        }
        lits1.addAll(lits2);
        if (labelId != null){
            List<SyTask> lits3 = new ArrayList<>();
            for (SyTask item : lits1){
                if (labelId.equals(item.getLabelId())){
                    lits3.add(item);
                }
            }
            return AjaxResult.success(lits3);
        }
        return AjaxResult.success(lits1);
    }

    /**
     * 更新任务
     */
    @Override
    public AjaxResult<?> update(SyTask task){
        List<SyTask> list = getTaskList();
        if (list == null){
            return AjaxResult.failure("系统错误!");
        }
        String taskId = task.getTaskId();
        for (int i = 0;i < list.size(); i++){
            if (taskId.equals(list.get(i).getTaskId())){
                if (list.get(i).getTaskSts() == YesOrNoStatusEnum.YES){
                    return AjaxResult.failure("已完成任务不支持更新");
                }
                list.get(i).setTaskTitle(task.getTaskTitle())
                        .setTaskDesc(task.getTaskDesc());
                if (list.get(i).getTaskType() == TaskTypeEnum.TODAY){
                    list.get(i).setLabelId(task.getLabelId());
                }
            }
        }
        CacheContext.set(getToken() + "task",list, 60*24*365);
        return AjaxResult.success("更新成功");
    }

    /**
     * 任务完成
     */
    @Override
    public AjaxResult<?> complete(String taskId){
        List<SyTask> list = getTaskList();
        if (list == null){
            return AjaxResult.failure("系统错误!");
        }
        for (int i = 0;i < list.size(); i++){
            if (taskId.equals(list.get(i).getTaskId())){
                list.get(i).setTaskSts(YesOrNoStatusEnum.YES);
            }
        }
        CacheContext.set(getToken() + "task",list, 60*24*365);
        return AjaxResult.success("任务完成");
    }

    /**
     * 任务转移
     */
    @Override
    public AjaxResult<?> transfer(String taskId) {
        List<SyTask> list = getTaskList();
        if (list == null){
            return AjaxResult.failure("系统错误!");
        }
        for (int i = 0;i < list.size(); i++){
            if (taskId.equals(list.get(i).getTaskId())){
                if (list.get(i).getTaskType() == TaskTypeEnum.TODAY){
                    list.get(i).setTaskType(TaskTypeEnum.TOTAL);
                }else{
                    list.get(i).setTaskType(TaskTypeEnum.TODAY);
                }
            }
        }
        CacheContext.set(getToken() + "task",list, 60*24*365);
        return AjaxResult.success("操作完成");
    }


    /**
     * 获取当前用户的任务数据列表
     */
    private List<SyTask> getTaskList(){
        String key = getToken() + "task";
        List<SyTask> list =  (List<SyTask>)CacheContext.get(key);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return  list;
    }
}
