package com.lushihao.aicode.ai.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流式消息相应基类
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-03   13:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamMessage {
    /**
     * 消息类型
     */
    private String type;
}
