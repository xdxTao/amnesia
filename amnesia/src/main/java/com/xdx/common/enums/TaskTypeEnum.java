package com.xdx.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 任务枚举
 */
public enum  TaskTypeEnum implements IBaseEnum<Integer, String, TaskTypeEnum> {
    TODAY(0, "今日任务"),
    TOTAL(1, "总任务");

    private Integer code;
    private String msg;

    private TaskTypeEnum(Integer status, String msg) {
        this.code = status;
        this.msg = msg;
        this.initMap(status, this);
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @JsonValue
    @Override
    public Integer getCode() {
        return this.code;
    }

    @JsonCreator
    public static TaskTypeEnum forValue(Integer code) {
        return (TaskTypeEnum)IBaseEnum.get(TaskTypeEnum.class, code);
    }
}

