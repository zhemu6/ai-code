package com.lushihao.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 应用部署请求类
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   15:08
 */
@Data
public class AppDeployRequest implements Serializable {

    private static final long serialVersionUID = -5819473575718515062L;
    /**
     * 应用 id
     */
    private Long appId;
}
