package com.lushihao.aicode.model.dto.chathistory;

import com.lushihao.aicode.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对话历史查询请求
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   18:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatHistoryQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 5846815915518223343L;

    /**
     * id
     */
    private Long id;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息类型（user/ai）
     */
    private String messageType;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 游标查询 - 最后一条记录的创建时间
     * 用于分页查询，获取早于此时间的记录 查询 <lastCreateTime
     */
    private LocalDateTime lastCreateTime;
}
