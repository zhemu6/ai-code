package com.lushihao.aicode.ratelimiter.enums;

/**
 * 限流类型
 * @author lushihao
 */

public enum RateLimitType {
    
    /**
     * 接口级别限流
     */
    API,
    
    /**
     * 用户级别限流
     */
    USER,
    
    /**
     * IP级别限流
     */
    IP
}
