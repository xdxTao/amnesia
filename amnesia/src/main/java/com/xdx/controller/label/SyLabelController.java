package com.xdx.controller.label;

import com.fasterxml.jackson.databind.JsonNode;
import com.xdx.common.common.AjaxResult;
import com.xdx.common.utils.CheckParams;
import com.xdx.common.utils.JsonUtils;
import com.xdx.common.utils.Tools;
import com.xdx.entitys.pojo.SyLabel;
import com.xdx.service.label.SyLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 标签管理
 *
 * @author 小道仙
 * @date 2020/8/8
 */
@RestController
public class SyLabelController {

    @Autowired
    private SyLabelService syLabelService;

    /**
     * 新增标签
     */
    @PostMapping("/label/add")
    public AjaxResult<?> add(@RequestBody Map<String,Object> params){

        AjaxResult check = CheckParams.check(params,  "labelName");
        if (!check.getSuccess()){
            return check;
        }
        if(!Tools.safeCheck(params.get("labelName").toString())){
            return AjaxResult.failure("存在非法字符，请重新输入");
        }
        return syLabelService.add(params);
    }

    /**
     * 修改标签
     */
    @PostMapping("/label/update")
    public AjaxResult<?> update(@RequestBody SyLabel syLabel){
        if (syLabel == null || syLabel.getLabelId() == null || syLabel.getLabelId().equals("")){
            return AjaxResult.failure("缺少必要参数：labelId");
        }
        if(!Tools.safeCheck(syLabel.getLabelName())){
            return AjaxResult.failure("存在非法字符，请重新输入");
        }
        return syLabelService.update(syLabel);
    }

    /**
     * 删除标签
     */
    @PostMapping("/label/delete")
    public AjaxResult<?> delete(@RequestBody Map<String,Object> params){
        AjaxResult check = CheckParams.check(params, "labelId");
        if (!check.getSuccess()){
            return check;
        }
        return syLabelService.delete(params);
    }

    /**
     * 标签列表
     */
    @PostMapping("/label/list")
    public AjaxResult<?> list(){
        return syLabelService.list();
    }

    /**
     * 修改默认标签
     */
    @PostMapping("/label/updateDefault")
    public AjaxResult<?> updateDefault(@RequestBody Map<String,Object> params){
        AjaxResult check = CheckParams.check(params, "userId","labelId","status");
        if (!check.getSuccess()){
            return check;
        }
        return syLabelService.updateDefault(params);
    }

    /**
     * 标签列表带统计
     */
    @GetMapping("/label/listAll")
    public AjaxResult<?> listAll(Integer flag){
        return syLabelService.listAll(flag);
    }

    /**
     * 获取默认标签Id
     */
    @PostMapping("/label/getDefaultLabel")
    public AjaxResult<?> getDefaultLabel(){
        return syLabelService.getDefaultLabel();
    }

    /**
     * 标签排序
     */
    @PostMapping("/label/sort")
    public AjaxResult<?> sortLabel(@RequestBody Object sortList){
        JsonNode jsonNode = JsonUtils.stringToJsonNode(JsonUtils.objectToJson(sortList));
        List<SyLabel> syLabels = JsonUtils.objectToList(jsonNode.get("sortList"), SyLabel.class);
        return syLabelService.sortLabel(syLabels);
    }

}
