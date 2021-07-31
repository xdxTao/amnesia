package com.xdx.entitys.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务模板
 *
 * @author 小道仙
 * @date 2020年12月26日
 */
@Data
@Accessors(chain = true)
@ToString
public class SyTemplate implements Serializable {
    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 标签id
     */
    private Integer labelId;

    /**
     * 模板标题
     */
    private String tmpTitle;

    /**
     * 模板描述
     */
    private String tmpDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 排序
     */
    private Integer sort;

    private static final long serialVersionUID = 1L;
}