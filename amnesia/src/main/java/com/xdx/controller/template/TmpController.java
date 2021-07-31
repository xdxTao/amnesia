package com.xdx.controller.template;

import com.fasterxml.jackson.databind.JsonNode;
import com.xdx.common.common.AjaxResult;
import com.xdx.common.utils.JsonUtils;
import com.xdx.entitys.pojo.SyLabel;
import com.xdx.entitys.pojo.SyTemplate;
import com.xdx.service.tmplate.TmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务模板
 *
 * @author 小道仙
 * @date 2020年12月26日
 */
@RestController
public class TmpController {

    @Autowired
    private TmpService tmpService;

    /**
     * 新增任务
     */
    @PostMapping("/tmp/add")
    public AjaxResult<?> add(@RequestBody SyTemplate tmp){
        if (tmp == null){
            return AjaxResult.failure("参数异常");
        }
        if (tmp.getLabelId() == null){
            return AjaxResult.failure("请选择标签");
        }
        if (tmp.getTmpTitle() == null){
            return AjaxResult.failure("请输入标题");
        }
        return tmpService.add(tmp);
    }

    /**
     * 删除任务
     */
    @GetMapping("/tmp/del")
    public AjaxResult<?> del(@RequestParam Integer id){
        if (id == null){
            return AjaxResult.failure("请选择要删除的模板");
        }
        return tmpService.del(id);
    }

    /**
     * 更新任务
     */
    @PostMapping("/tmp/update")
    public AjaxResult<?> update(@RequestBody SyTemplate tmp){
        if (tmp == null){
            return AjaxResult.failure("参数异常");
        }
        if (tmp.getId() == null){
            return AjaxResult.failure("请选择要更新的模板");
        }
        return tmpService.update(tmp);
    }

    /**
     * 任务列表
     */
    @GetMapping("/tmp/list")
    public AjaxResult<?> list(@RequestParam Integer labelId){
        if (labelId == null){
            return AjaxResult.failure("参数异常");
        }
        return tmpService.list(labelId);
    }

    /**
     * 复制
     */
    @PostMapping("/tmp/copy")
    public AjaxResult<?> copy(@RequestBody String id){
        JsonNode jsonNode = JsonUtils.stringToJsonNode(id);
        List<String> ids = JsonUtils.jsonToList(jsonNode.get("ids").toString(), String.class);
        if (ids == null || ids.isEmpty()){
            return AjaxResult.failure("参数异常");
        }
        return tmpService.copy(ids);
    }

    /**
     * 模板排序
     */
    @PostMapping("/tmp/sort")
    public AjaxResult<?> sort(@RequestBody Object sortList){
        JsonNode jsonNode = JsonUtils.stringToJsonNode(JsonUtils.objectToJson(sortList));
        List<SyTemplate> syTemplates = JsonUtils.objectToList(jsonNode.get("sortList"), SyTemplate.class);
        return tmpService.sort(syTemplates);
    }
}
