package com.xdx.service.other.impl;

import com.xdx.common.common.AjaxResult;
import com.xdx.common.common.MyCommonService;
import com.xdx.common.enums.YesOrNoStatusEnum;
import com.xdx.entitys.pojo.SyEdition;
import com.xdx.mapper.other.SyEditionMapper;
import com.xdx.service.other.OtherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 其它操作
 *
 * @author 小道仙
 * @date 2020年12月25日
 */
@Service
public class OtherServiceImpl extends MyCommonService implements OtherService {

    @Autowired
    private SyEditionMapper editionMapper;

    /**
     * 版本信息
     */
    @Override
    public AjaxResult<?> editionInfo() {
        List<SyEdition> main = editionMapper.selectList(new SyEdition().setEditionMain(YesOrNoStatusEnum.YES),"desc","id");
        for (SyEdition item : main){
            item.setLists(editionMapper.selectList(new SyEdition().setSupId(item.getId()),null,"id"));
        }
        return AjaxResult.success(main);
    }
}
