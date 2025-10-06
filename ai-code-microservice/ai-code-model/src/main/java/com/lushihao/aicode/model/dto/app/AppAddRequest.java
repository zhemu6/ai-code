package com.lushihao.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建应用
 * @author: lushihao
 * @version: 1.0
 *
 * create:   2025-09-02   13:46
 */
@Data
public class AppAddRequest  implements Serializable {

    private static final long serialVersionUID = -4428985601357480798L;

    /**
     * 应用初始化的 prompt
     */
    private String initPrompt;

}
