package com.xdx.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.xdx.common.common.AjaxResult;
import com.xdx.common.enums.YesOrNoStatusEnum;
import com.xdx.common.utils.AccessToken;
import com.xdx.entitys.pojo.SyUser;
import com.xdx.mapper.user.SyUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author 小道仙
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private SyUserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String token = request.getHeader("token");
//        if (token != null){
//            SyUser user = userMapper.selectOne(new SyUser().setWxOpenId(token)
//                    .setUserStatus(YesOrNoStatusEnum.YES));
//            if (user != null){
//                return true;
//            }
//        }
//        response.setContentType("text/html;charset=utf-8");
//        response.getWriter().write(JSON.toJSONString(AjaxResult.failure("系统异常!")));
//        return false;
        return true;

    }
}
