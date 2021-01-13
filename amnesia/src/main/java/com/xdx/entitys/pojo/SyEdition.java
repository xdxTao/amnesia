package com.xdx.entitys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xdx.common.enums.YesOrNoStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 版本信息
 */
@Data
@Accessors(chain = true)
public class SyEdition {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private YesOrNoStatusEnum editionMain;

    private String editionInfo;

    private Integer supId;

    private Date createTime;

    @TableField(exist = false)
    private List<SyEdition> lists;
}
