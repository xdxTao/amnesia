package com.xdx.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum YesOrNoStatusEnum implements IBaseEnum<Integer, String, YesOrNoStatusEnum> {
    NO(0, "否"),
    YES(1, "是");

    private Integer code;
    private String msg;

    private YesOrNoStatusEnum(Integer status, String msg) {
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
    public static YesOrNoStatusEnum forValue(Integer code) {
        return (YesOrNoStatusEnum)IBaseEnum.get(YesOrNoStatusEnum.class, code);
    }
}
