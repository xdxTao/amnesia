package com.xdx.mapper.label;

import com.xdx.common.common.MyBaseMapper;
import com.xdx.entitys.pojo.SyLabel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SyLabelMapper extends MyBaseMapper<SyLabel> {

    void addLabel(Map<String, Object> params);

    void deleteLabel(Map<String, Object> params);

    List<SyLabel> list(@Param("userId") String userId);

    void updateDefault(@Param("userId") String userId,@Param("labelId") String labelId);

    List<Map<String, Object>> listAll(@Param("userId") String userId);
}
