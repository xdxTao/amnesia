package com.xdx.entitys.dto;

import com.xdx.common.enums.TaskTypeEnum;
import com.xdx.common.enums.YesOrNoStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 任务实体
 */
@Data
@Accessors(chain = true)
public class SyTask {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务类型
     */
    private Integer labelId;

    /**
     * 任务标题
     */
    private String taskTitle;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 任务状态
     */
    private YesOrNoStatusEnum taskSts;

    /**
     * 任务类型
     */
    private TaskTypeEnum taskType;
}
