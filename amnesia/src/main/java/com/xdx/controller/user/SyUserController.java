package com.xdx.controller.user;

import com.xdx.common.common.AjaxResult;
import com.xdx.service.user.SyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户相关操作
 */
@RestController
public class SyUserController {

    @Autowired
    private SyUserService syUserService;

    /**
     * 用户登录
     *
     * @param code
     * @return
     */
    @GetMapping("/user/login")
    public AjaxResult<?> login(@RequestParam String code){
        if (code == null || code.equals("")){
            return AjaxResult.failure("确少参数 code");
        }
        return syUserService.login(code);
    }

    /**
     * 判断用户是否授权
     */
    @GetMapping("/user/isAuthorize")
    public AjaxResult<?> isAuthorize(){

        return syUserService.isAuthorize();
    }

    /**
     * 用户授权
     */
    @GetMapping("/user/authorize")
    public AjaxResult<?> authorize(){

        return syUserService.authorize();
    }

    /**
     * 用户编号
     */
    @GetMapping("/user/userNumber")
    public AjaxResult<?> userNumber(){

        return syUserService.userNumber();
    }

    /**
     * 同步授权状态
     */
    @GetMapping("/user/synAuthorize")
    public AjaxResult<?> synAuthorize(Boolean authorize){
        if (authorize == null){
            return AjaxResult.failure("系统错误");
        }
        return syUserService.synAuthorize(authorize);
    }

}
