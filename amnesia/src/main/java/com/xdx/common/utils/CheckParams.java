package com.xdx.common.utils;

import com.xdx.common.common.AjaxResult;

import java.util.Map;

/**
 * 参数校验
 *
 * @author 小道仙
 * @date 2020年8月9日
 */
public class CheckParams {

    public static AjaxResult check(Map<String, Object> params,String ...checkParams){
        AjaxResult result = AjaxResult.failure("参数为空");
        try {
            if (params == null || params.isEmpty()){
                return result;
            }
            for (String item : checkParams){
                Object object = params.get(item);
                if (object == null || object.equals("")){
                    return result.setErrDesc("缺少必要参数 : "+ item);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }
        return AjaxResult.success("参数校验成功");
    }
}
