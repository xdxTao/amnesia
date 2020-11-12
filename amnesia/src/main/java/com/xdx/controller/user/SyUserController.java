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
     */
    @GetMapping("/user/login")
    public AjaxResult<?> login(@RequestParam String code){
        if (code == null || code.equals("")){
            return AjaxResult.failure("确少参数 code");
        }
        return syUserService.login(code);
    }

    /**
     * 修改用户使用阅读
     */
    @GetMapping("/user/completeHelpRead")
    public AjaxResult<?> completeHelpRead(){
        return syUserService.completeHelpRead();
    }

    /**
     * 用户编号
     */
    @GetMapping("/user/userNumber")
    public AjaxResult<?> userNumber(){
        return syUserService.userNumber();
    }
}
