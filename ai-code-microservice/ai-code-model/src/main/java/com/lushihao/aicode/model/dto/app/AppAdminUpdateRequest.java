package com.lushihao.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理员更新应用请求
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   14:07
 */
@Data
public class AppAdminUpdateRequest implements Serializable {

    private static final long serialVersionUID = -3820754573110301798L;

    /**
     * id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用封面
     */
    private String cover;

    /**
     * 优先级
     */
    private Integer priority;

}
