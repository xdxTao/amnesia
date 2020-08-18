package com.xdx.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.xdx.common.common.AjaxResult;
import com.xdx.common.common.MyCommonService;
import com.xdx.common.enums.YesOrNoStatusEnum;
import com.xdx.common.utils.HttpClient;
import com.xdx.entitys.config.AppletConfig;
import com.xdx.entitys.pojo.SyUser;
import com.xdx.mapper.user.SyUserMapper;
import com.xdx.service.user.SyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SyUserServiceImpl  extends MyCommonService
        implements SyUserService {

    @Autowired
    private AppletConfig appletConfig;

    @Autowired
    private SyUserMapper syUserMapper;

    @Override
    public AjaxResult<Object> login(String code) {
        // 1、去获取用户的openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", appletConfig.getAppid());
        map.put("secret", appletConfig.getSecret());
        map.put("grantType", appletConfig.getGrantType());
        map.put("js_code", code);
        Object obj = HttpClient.requestGet(appletConfig.getLoginUrl(), map);
        JSONObject jsonObject = JSONObject.parseObject(obj.toString());
        String openId = jsonObject.get("openid").toString();

        // 2、判断用户是否存在 如果不存在就新增用户
        SyUser user = syUserMapper.selectOne(new SyUser().setWxOpenId(openId).setUserStatus(YesOrNoStatusEnum.YES));
        if (user == null){
            user = new SyUser();
            user.setWxOpenId(openId)
                    .setUserStatus(YesOrNoStatusEnum.YES)
                    .setIsAuthorize(YesOrNoStatusEnum.NO);
            syUserMapper.insert(user);
            user = syUserMapper.selectOne(new SyUser().setWxOpenId(openId));
        }
        // 3、返回登录openId
        return AjaxResult.success((Object)openId);
    }

    @Override
    public AjaxResult<?> isAuthorize() {
        String token = getToken();
        SyUser user = syUserMapper.selectOne(new SyUser().setWxOpenId(token));
        boolean result = false;
        if (user.getIsAuthorize() == YesOrNoStatusEnum.YES){
            result = true;
        }
        return AjaxResult.success(result);
    }

    @Override
    public AjaxResult<?> authorize() {
        String token = getToken();
        SyUser user = syUserMapper.selectOne(new SyUser().setWxOpenId(token));
        SyUser update = new SyUser();
        update.setUserId(user.getUserId())
                .setIsAuthorize(YesOrNoStatusEnum.YES);
        syUserMapper.updateById(update);
        return AjaxResult.success(true);
    }

    @Override
    public AjaxResult<?> userNumber() {
        String token = getToken();
        SyUser user = syUserMapper.selectOne(new SyUser().setWxOpenId(token));
        if (user == null){
            return AjaxResult.failure("系统异常");
        }
        return AjaxResult.success(user.getUserId());
    }

    @Override
    public AjaxResult<?> synAuthorize(Boolean authorize) {

        String token = getToken();
        SyUser user = syUserMapper.selectOne(new SyUser().setWxOpenId(token));
        SyUser update = new SyUser().setUserId(user.getUserId());
        if (authorize){
            update.setIsAuthorize(YesOrNoStatusEnum.YES);
        }else{
            update.setIsAuthorize(YesOrNoStatusEnum.NO);
        }
        syUserMapper.updateById(update);
        return AjaxResult.success();
    }

}
