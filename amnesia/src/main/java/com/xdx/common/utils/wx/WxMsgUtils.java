package com.xdx.common.utils.wx;

import com.xdx.common.utils.HttpClient;
import com.xdx.common.utils.JsonUtils;
import com.xdx.common.utils.wx.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送微信消息
 *
 * 文档地址: https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/uniform-message/uniformMessage.send.html
 * @author 小道仙
 * @date 2021年4月19日
 */
@Slf4j
public class WxMsgUtils {

    /**
     * 发送数据接口
     */
    private static final String MSG_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=";

    /**
     * 跳转页面地址
     */
    private static final String DEFAULT_PAGE = "pages/task/task";

    /**
     * 发送模板消息
     *
     * @param touser 接收者（用户）的 openid
     * @param template_id 所需下发的订阅模板id
     * @param page 点击模板卡片后的跳转页面，仅限本小程序内的页面。默认地址【DEFAULT_PAGE】，传递NO，表示不需要地址
     * @param miniprogram_state 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     * @param data 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } }
     *
     * @return true 表示成功， false 表示失败
     */
    public static String sendTmplMsg(String touser, String template_id, String page, String miniprogram_state,Map<String,Map<String,String>> data){
        Map<String,Object> params = new HashMap<>();
        params.put("access_token", AccessToken.getAccessToken());
        params.put("touser", touser);
        params.put("template_id",template_id);
        params.put("data",data);
        if (miniprogram_state != null && !"".equals(miniprogram_state)){
            params.put("miniprogram_state", miniprogram_state);
        }
        if (StringUtils.isEmpty(page)){
            params.put("page",DEFAULT_PAGE);
        }
        try {
            String post = HttpClient.post2(MSG_SEND_URL+AccessToken.getAccessToken(), params);
            Map<String, Object> map = JsonUtils.jsonToMap(post);
            if (map != null && Integer.parseInt(map.get("errcode").toString()) != 0){
                log.error("微信模板消息发送失败：" + map.get("errmsg"));
            }
            return map.get("errcode").toString();
        }catch (Exception e){
            log.error("微信模板消息发送异常", e);
            return "500";
        }
    }

}
