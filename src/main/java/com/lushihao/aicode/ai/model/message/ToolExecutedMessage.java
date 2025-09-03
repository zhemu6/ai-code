package com.lushihao.aicode.ai.model.message;

import dev.langchain4j.service.tool.ToolExecution;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 工具执行结果消息
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-03   13:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ToolExecutedMessage extends StreamMessage {
    /**
     * 工具请求id
     */
    private String id;
    /**
     * 工具名称
     */
    private String name;
    /**
     * 工具参数
     */
    private String arguments;
    /**
     * 工具执行结果
     */
    private String result;

    public ToolExecutedMessage(ToolExecution toolExecution) {
        super(StreamMessageTypeEnum.TOOL_EXECUTED.getValue());
        this.id = toolExecution.request().id();
        this.name = toolExecution.request().name();
        this.arguments = toolExecution.request().arguments();
        this.result = toolExecution.result();
    }
}
