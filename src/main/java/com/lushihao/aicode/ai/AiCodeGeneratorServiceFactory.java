package com.lushihao.aicode.ai;

import cn.hutool.ai.core.AIService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI 服务创建工厂
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-01   22:40
 */
@Configuration
public class AiCodeGeneratorServiceFactory {

    @Resource
    private ChatModel chatModel;

    @Resource
    private StreamingChatModel streamingChatModel;

    /**
     * 创建AI 代码生成器服务
     *
     * @return
     */
    @Bean
    public AiCodeGeneratorService aiCodeGeneratorService() {
        return AiServices
                .builder(AiCodeGeneratorService.class)
                .chatModel(chatModel)
                .streamingChatModel(streamingChatModel)
                .build();
    }

}
