package com.xdx.service.label;

import com.xdx.common.common.AjaxResult;
import com.xdx.entitys.pojo.SyLabel;

import java.util.Map;

public interface SyLabelService {

    /**
     * 新增标签
     */
    AjaxResult<?> add(Map<String,Object> params);

    /**
     * 修改标签
     */
    AjaxResult<?> update(SyLabel syLabel);

    /**
     * 删除标签
     */
    AjaxResult<?> delete(Map<String, Object> params);

    /**
     * 标签列表
     */
    AjaxResult<?> list();

    /**
     * 修改默认标签
     */
    AjaxResult<?> updateDefault(Map<String, Object> params);

    /**
     * 标签列表带统计
     */
    AjaxResult<?> listAll(Integer flag);

    /**
     * 获取默认标签Id
     */
    AjaxResult<?> getDefaultLabel();
}
