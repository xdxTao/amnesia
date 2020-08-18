package com.xdx.entitys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xdx.common.enums.YesOrNoStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;
/**
 * 标签实体
 */
@Data
@Accessors(chain = true)
public class SyLabel {

    @TableId(value = "label_id",type = IdType.AUTO)
    private String labelId;

    private Integer userId;

    private String labelName;

    private Integer labelSort;

    private YesOrNoStatusEnum labelStatus;

    private YesOrNoStatusEnum labelDefault;
}
