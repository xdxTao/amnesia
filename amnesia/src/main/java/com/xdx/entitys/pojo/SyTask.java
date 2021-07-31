package com.xdx.entitys.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xdx.common.enums.TaskTypeEnum;
import com.xdx.common.enums.YesOrNoStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

import static com.baomidou.mybatisplus.annotation.IdType.AUTO;

/**
 * 任务实体
 */
@Data
@Accessors(chain = true)
public class SyTask {

    /**
     * 任务id
     */
    @TableId(type = AUTO)
    private Integer taskId;

    /**
     * 用户id
     */
    private Integer userId;

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
     * 任务排序
     */
    private Integer taskSort;

    /**
     * 任务状态
     */
    private YesOrNoStatusEnum taskSts;

    /**
     * 任务类型
     */
    private TaskTypeEnum taskType;

    /**
     * 是否删除 0 没有删除，1 已经删除
     */
    private YesOrNoStatusEnum taskDel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 代办通知时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date taskNoticeTime;

    /**
     * 通知状态（1 已通知）
     */
    private Integer taskNoticeStatus;
}
