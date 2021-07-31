package com.xdx.service.tmplate;

import com.xdx.common.common.AjaxResult;
import com.xdx.entitys.pojo.SyLabel;
import com.xdx.entitys.pojo.SyTemplate;

import java.util.List;

public interface TmpService {

    AjaxResult<?> add(SyTemplate tmp);

    AjaxResult<?> del(Integer id);

    AjaxResult<?> update(SyTemplate tmp);

    AjaxResult<?> list(Integer labelId);

    AjaxResult<?> copy(List<String> labelId);

    AjaxResult<?> sort(List<SyTemplate> syTemplates);
}
