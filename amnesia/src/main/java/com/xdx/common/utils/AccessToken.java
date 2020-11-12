package com.xdx.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取 AccessToken
 *
 * @author 小道仙
 * @date 2020年10月18日
 */
@Component
public class AccessToken {

    private static String accountToken = "";

    private static Long expireDate;

    private static String grantType = "client_credential";

    private static String appId;

    @Value("${wx.appid}")
    public void setAppId(String appId){
        AccessToken.appId = appId;
    }

    private static String secret;

    @Value("${wx.secret}")
    public void setSecret(String secret){
        AccessToken.secret = secret;
    }

    public static String getAccessToken(){
        if (accountToken.equals("") || expireDate == null || expireDate < new Date().getTime()){
            Map<String,String> params = new HashMap<>();
            params.put("grant_type", grantType);
            params.put("appid", appId);
            params.put("secret", secret);
            Object o = HttpClient.requestGet("https://api.weixin.qq.com/cgi-bin/token", params);
            JsonNode jsonNode = JsonUtils.stringToJsonNode(o.toString());
            accountToken = jsonNode.get("access_token").toString().replace("\"","");
            expireDate = new Date().getTime() + (Integer.parseInt(jsonNode.get("expires_in").toString()) * 1000);
        }
        return accountToken;
    }
}