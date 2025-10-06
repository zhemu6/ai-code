package com.lushihao.aicode.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;

/**
 * 根据对象缓存key工具类
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-05   17:00
 */
public class CacheKeyUtils {
    /**
     * 根据对象生成缓存key JOSN+ md5
     * @param object 要生成key的对象
     * @return MD5哈希后的key
     */
    public static String generateCacheKey(Object object){
        if(object==null){
            return DigestUtil.md5Hex("null");
        }
        // 对象转成JSON字符串
        String jsonStr = JSONUtil.toJsonStr(object);
        // 对JSON字符串进行MD5加密
        return DigestUtil.md5Hex(jsonStr);

    }
}
