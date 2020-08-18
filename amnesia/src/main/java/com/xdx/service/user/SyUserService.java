package com.xdx.service.user;

import com.xdx.common.common.AjaxResult;

public interface SyUserService {


    /**
     * 用户登录
     */
    AjaxResult<Object> login(String code);

    /**
     * 判断用户是否授权
     */
    AjaxResult<?> isAuthorize();

    /**
     * 用户授权
     */
    AjaxResult<?> authorize();

    /**
     * 用户编号
     */
    AjaxResult<?> userNumber();

    /**
     * 同步授权状态
     */
    AjaxResult<?> synAuthorize(Boolean authorize);
}
