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
                    .setIsAuthorize(YesOrNoStatusEnum.NO)
                    .setHelpRead(0);
            syUserMapper.insert(user);
        }
        // 3、返回登录openId
        Map<String,Object> result = new HashMap<>();
        result.put("openId",openId);
        result.put("helpRead", user.getHelpRead());
        return AjaxResult.success(result);
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
    public AjaxResult<?> completeHelpRead() {
        SyUser curUser = getCurUser();
        curUser.setHelpRead(1);
        syUserMapper.updateById(curUser);
        return null;
    }
}
