package com.xdx.entitys.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 小程序配置
 */
@Data
@Component
public class AppletConfig {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.grantType}")
    private String grantType;

    @Value("${wx.loginUrl}")
    private String loginUrl;
}
