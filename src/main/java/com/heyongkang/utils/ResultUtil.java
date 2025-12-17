package com.heyongkang.utils;

import java.util.HashMap;
import java.util.Map;

public class ResultUtil {

    /**
     * 成功响应
     */
    public static Map<String, Object> success() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("code", 200);
        result.put("message", "操作成功");
        return result;
    }

    /**
     * 成功响应（带数据）
     */
    public static Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("code", 200);
        result.put("message", "操作成功");
        result.put("data", data);
        return result;
    }

    /**
     * 失败响应
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", 500);
        result.put("message", message);
        return result;
    }

    /**
     * 失败响应（带错误码）
     */
    public static Map<String, Object> error(int code, String message) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", code);
        result.put("message", message);
        return result;
    }
}