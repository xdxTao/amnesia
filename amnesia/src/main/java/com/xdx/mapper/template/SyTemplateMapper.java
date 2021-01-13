package com.xdx.mapper.template;

import com.xdx.common.common.MyBaseMapper;
import com.xdx.entitys.pojo.SyTask;
import com.xdx.entitys.pojo.SyTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SyTemplateMapper extends MyBaseMapper<SyTemplate> {

    /**
     * 根据ids查询
     */
    List<SyTemplate> selectByIds(@Param("ids") List<String> ids);
}
