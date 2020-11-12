package com.xdx.entitys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xdx.common.enums.YesOrNoStatusEnum;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户实体
 */
@Data
@Accessors(chain = true)
@ToString
public class SyUser  {
    /**
     * 用户id
     */
    @TableId(value = "user_id",type = IdType.AUTO)
    private Integer userId;

    /**
     * 微信OpenId
     */
    private String wxOpenId;

    /**
     * 用户状态:1启用0停用
     */
    private YesOrNoStatusEnum userStatus;

    /**
     * 是否授权1已授权0未授权
     */
    private YesOrNoStatusEnum isAuthorize;

    /**
     * 用户性别 0 未知、1 男性，2 女性
     */
    private Integer gender;

    /**
     * 备注
     */
    private String userRemarks;

    /**
     * 使用帮助是否阅读 1 已经阅读
     */
    private Integer helpRead;

}