package com.xdx.common.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

public class Tools {


    /**
     * 文字校验
     */
    public static boolean safeCheck(String content){
        if (content == null || content.equals("")){
            return true;
        }
        boolean result = false;
        try {
            Map<String,String> params = new HashMap<>();
            params.put("content", content);
            String data = HttpClient.post("https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+AccessToken.getAccessToken(), params);
            JsonNode jsonNode = JsonUtils.stringToJsonNode(data);
            if (jsonNode.get("errmsg").toString().replace("\"","").equals("ok")){
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
