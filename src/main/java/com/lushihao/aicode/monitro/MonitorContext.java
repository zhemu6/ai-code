package com.lushihao.aicode.monitro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-09   15:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorContext implements Serializable {

    private static final long serialVersionUID = 5011024965848154169L;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 应用id
     */
    private String appId;

}
