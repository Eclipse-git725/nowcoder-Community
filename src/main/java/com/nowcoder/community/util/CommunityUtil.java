package com.nowcoder.community.util;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件名称: CommunityUtil.java
 * 作者: gxy
 * 创建日期: 2025/2/25
 * 描述: 提供静态方法
 */
public class CommunityUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    public static String md5(String key) {
        if(StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    // 获取 JSON 字符串
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        // 将参数封装到 JSON 对象
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if(map != null) {
            for(String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }

        return json.toString();
    }

    // 重载
    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "gxy");
        map.put("age", 23);
        System.out.println(getJSONString(0, "ok", map));
    }
}
