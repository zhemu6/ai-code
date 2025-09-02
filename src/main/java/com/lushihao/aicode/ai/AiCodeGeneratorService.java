package com.lushihao.aicode.ai;

import com.lushihao.aicode.ai.model.HtmlCodeResult;
import com.lushihao.aicode.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

/**
 * 按照LangChain4j的AI service开发模式 创建服务接口
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-01   22:36
 */
public interface AiCodeGeneratorService {

    /**
     * 生成 HTML 代码
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果 HtmlCodeResult 里面包含html代码和描述
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    /**
     * 生成 多文件 代码
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);


    /**
     * 流式调用生成 HTML 代码
     *
     * @param userMessage 用户消息
     * @return Flux<String> 对象 用于流式返回
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    /**
     * 流式调用生成 多文件 代码
     *
     * @param userMessage 用户消息
     * @return Flux<String> 对象 用于流式返回
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String userMessage);
}
