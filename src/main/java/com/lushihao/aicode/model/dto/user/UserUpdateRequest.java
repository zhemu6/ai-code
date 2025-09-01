package com.lushihao.aicode.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-01   16:32
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 2573211695346077127L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;


}
