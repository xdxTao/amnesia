package com.xdx.service.user;

import com.xdx.common.common.AjaxResult;

public interface SyUserService {

    /**
     * 用户登录
     */
    AjaxResult<Object> login(String code);

    /**
     * 用户编号
     */
    AjaxResult<?> userNumber();

    AjaxResult<?> completeHelpRead();

}
