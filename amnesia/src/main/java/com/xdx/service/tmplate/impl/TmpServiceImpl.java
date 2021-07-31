package com.xdx.service.tmplate.impl;

import com.xdx.common.common.AjaxResult;
import com.xdx.common.common.MyCommonService;
import com.xdx.common.enums.TaskTypeEnum;
import com.xdx.common.enums.YesOrNoStatusEnum;
import com.xdx.entitys.pojo.SyLabel;
import com.xdx.entitys.pojo.SyTask;
import com.xdx.entitys.pojo.SyTemplate;
import com.xdx.mapper.task.SyTaskMapper;
import com.xdx.mapper.template.SyTemplateMapper;
import com.xdx.service.tmplate.TmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TmpServiceImpl extends MyCommonService implements TmpService {

    @Autowired
    private SyTemplateMapper tmpMapper;

    @Autowired
    private SyTaskMapper taskMapper;

    @Override
    public AjaxResult<?> add(SyTemplate tmp) {
        tmp.setCreateTime(new Date());
        tmp.setUserId(getCurUser().getUserId());
        tmpMapper.insert(tmp);
        return AjaxResult.success("新增成功");
    }

    @Override
    public AjaxResult<?> del(Integer id) {
        tmpMapper.deleteById(id);
        return AjaxResult.success("删除成功");
    }

    @Override
    public AjaxResult<?> update(SyTemplate tmp) {
        tmp.setModifyTime(new Date());
        tmpMapper.updateById(tmp);
        return AjaxResult.success("更新成功");
    }

    @Override
    public AjaxResult<?> list(Integer labelId) {
        SyTemplate tmp = new SyTemplate();
        tmp.setLabelId(labelId);
        List<SyTemplate> syTemplates = tmpMapper.selectList(tmp,"asc","sort");
        return AjaxResult.success(syTemplates);
    }

    @Override
    public AjaxResult<?> copy(List<String> ids) {
        Integer userId = getCurUser().getUserId();
        List<SyTemplate> templates = tmpMapper.selectByIds(ids);
        SyTask task = new SyTask();
        task.setUserId(userId);
        Integer maxSort = 0;
        for (SyTemplate item : templates){
            maxSort = taskMapper.selectMaxSort(task.getUserId());
            if (maxSort == null){
                maxSort = 0;
            }
            task.setLabelId(item.getLabelId());
            task.setTaskSort(maxSort);
            task.setTaskDel(YesOrNoStatusEnum.NO);
            task.setCreateTime(new Date());
            task.setTaskSts(YesOrNoStatusEnum.NO);
            task.setTaskType(TaskTypeEnum.TOTAL);
            task.setTaskTitle(item.getTmpTitle());
            task.setTaskDesc(item.getTmpDesc());
            taskMapper.insert(task);
        }
        return AjaxResult.success("复制成功");
    }

    @Override
    public AjaxResult<?> sort(List<SyTemplate> syTemplates) {

        SyTemplate update = new SyTemplate();
        int i = 1;
        for (SyTemplate item : syTemplates){
            update.setId(item.getId());
            update.setSort(i++);
            tmpMapper.updateById(update);
        }
        return AjaxResult.success("操作完成");
    }
}
