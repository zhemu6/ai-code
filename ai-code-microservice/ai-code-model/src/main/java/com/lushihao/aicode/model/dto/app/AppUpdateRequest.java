package com.lushihao.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新应用
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   13:51
 */
@Data
public class AppUpdateRequest implements Serializable {

    private static final long serialVersionUID = 294485252012109473L;
    /**
     * id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;
}
