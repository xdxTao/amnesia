package com.xdx.controller.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.xdx.common.common.AjaxResult;
import com.xdx.common.utils.JsonUtils;
import com.xdx.entitys.dto.SyTask;
import com.xdx.entitys.pojo.SyLabel;
import com.xdx.service.task.SyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户相关操作
 */
@RestController
public class SyTaskController {

    @Autowired
    private SyTaskService taskService;

    /**
     * 新增任务
     */
    @PostMapping("/task/add")
    public AjaxResult<?> add(@RequestBody SyTask task){
        if (task == null || task.getTaskTitle() == null){
            return AjaxResult.failure("参数不全");
        }
        return taskService.add(task);
    }

    /**
     * 获取任务数据
     */
    @GetMapping("/task/list")
    public AjaxResult<List<SyTask>> list(@RequestParam Integer taskType, Integer labelId){

        return taskService.list(taskType,labelId);
    }

    /**
     * 更新任务 只更新任务标题和描述
     */
    @PostMapping("/task/update")
    public AjaxResult<?> update(@RequestBody SyTask task){
        if (task == null || "".equals(task.getTaskId())){
            return AjaxResult.failure("确少参数:taskId");
        }
        return taskService.update(task);
    }

    /**
     * 任务完成
     */
    @GetMapping("/task/complete")
    public AjaxResult<?> complete(@RequestParam String taskId){
        if (taskId == null || "".equals(taskId)){
            return AjaxResult.failure("确少参数:taskId");
        }
        return taskService.complete(taskId);
    }

    /**
     * 任务转移
     */
    @GetMapping("/task/transfer")
    public AjaxResult<?> transfer(@RequestParam String taskId){
        if (taskId == null || "".equals(taskId)){
            return AjaxResult.failure("确少参数:taskId");
        }
        return taskService.transfer(taskId);
    }

    /**
     * 任务排序
     */
    @PostMapping("/task/sort")
    public AjaxResult<?> taskSort(@RequestBody Object sortList){
        JsonNode jsonNode = JsonUtils.stringToJsonNode(JsonUtils.objectToJson(sortList));
        List<SyTask> syTasks = JsonUtils.objectToList(jsonNode.get("sortList"), SyTask.class);
        return taskService.taskSort(syTasks);
    }

}
