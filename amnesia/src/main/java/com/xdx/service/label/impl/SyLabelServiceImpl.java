package com.xdx.service.label.impl;

import com.xdx.common.common.AjaxResult;
import com.xdx.common.common.MyCommonService;
import com.xdx.common.enums.YesOrNoStatusEnum;
import com.xdx.entitys.pojo.SyLabel;
import com.xdx.mapper.label.SyLabelMapper;
import com.xdx.service.label.SyLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SyLabelServiceImpl extends MyCommonService implements SyLabelService {

    @Autowired
    private SyLabelMapper labelMapper;

    /**
     * 新增标签
     */
    @Override
    public AjaxResult<?> add(Map<String,Object> params){
        if (params.get("labelName").toString().length() != 2){
            return AjaxResult.failure("标签必须为2个字符");
        }
        Integer userId = getCurUser().getUserId();
        // 查看该用户的标签个数，不能超过4个
        Integer count = labelMapper.selectCount(new SyLabel().setUserId(userId));
        if (count >= 4){
            return AjaxResult.failure("最多只能创建4个标签");
        }
        params.put("userId", userId);
        labelMapper.addLabel(params);
        return AjaxResult.success("标签新增成功");
    }

    /**
     * 修改标签
     */
    @Override
    public AjaxResult<?> update(SyLabel syLabel) {
        if (syLabel.getLabelName() != null){
            if (syLabel.getLabelName().length() != 2){
                return AjaxResult.failure("标签必须为2个字符");
            }
        }
        labelMapper.update(syLabel,new SyLabel().setLabelId(syLabel.getLabelId()));
        return AjaxResult.success("标签修改成功");
    }

    /**
     * 删除标签
     */
    @Override
    public AjaxResult<?> delete(Map<String, Object> params) {
        labelMapper.deleteLabel(params);
        return AjaxResult.success("标签删除成功");
    }

    /**
     * 标签列表
     */
    @Override
    public AjaxResult<?> list() {
        SyLabel syLabel = new SyLabel().setUserId(getCurUser().getUserId());
        List<SyLabel> list = labelMapper.selectList(syLabel,"desc","label_sort");
        return AjaxResult.success(list);
    }

    /**
     * 修改默认标签
     */
    @Override
    public AjaxResult<?> updateDefault(Map<String, Object> params) {
        labelMapper.updateDefault(params.get("userId").toString(),"");
        if ((boolean)params.get("status")){
            labelMapper.updateDefault(params.get("userId").toString(),params.get("labelId").toString());
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 标签列表带统计
     */
    @Override
    public AjaxResult<?> listAll(Integer flag) {
        SyLabel syLabel = new SyLabel().setUserId(getCurUser().getUserId());
        syLabel.setLabelStatus(YesOrNoStatusEnum.YES);
        List<SyLabel> list = labelMapper.selectList(syLabel,"desc","label_sort");
        if (flag != null && flag == 1){
            SyLabel tmp = new SyLabel();
            tmp.setLabelId("");
            tmp.setLabelName("全部");
            list.add(0,tmp);
        }
        return AjaxResult.success(list);
    }

    /**
     * 获取默认标签
     */
    @Override
    public AjaxResult<?> getDefaultLabel() {
        SyLabel select = new SyLabel();
        select.setLabelDefault(YesOrNoStatusEnum.YES);
        select.setUserId(getCurUser().getUserId());
        select.setLabelStatus(YesOrNoStatusEnum.YES);
        String labelId = "";
        SyLabel syLabel = labelMapper.selectOne(select);
        if (syLabel != null){
            labelId = syLabel.getLabelId();
        }
        return AjaxResult.success((Object) labelId);
    }
}
