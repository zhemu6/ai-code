package com.lushihao.aicode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求列
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-27   17:00
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 8717015138081707510L;
    // 账号
    private String userAccount;
    // 密码
    private String userPassword;

}


