package com.lushihao.aicode.ai.model.message;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 工具调用消息
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-03   13:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToolRequestMessage extends StreamMessage {
    /**
     * 工具请求id
     */
    private String id;

    /**
     * 工具名称
     */
    private String name;

    /**
     *
     */
    private String arguments;

    /**
     * 存储工具调用的文本信息：
     */
    private String text;


    public ToolRequestMessage(ToolExecutionRequest toolExecutionRequest) {
        super(StreamMessageTypeEnum.TOOL_REQUEST.getValue());
        this.id = toolExecutionRequest.id();
        this.name = toolExecutionRequest.name();
        this.arguments = toolExecutionRequest.arguments();
    }
}
