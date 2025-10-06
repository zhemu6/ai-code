package com.lushihao.aicode.ai;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.lushihao.aicode.ai.guardrail.PromptSafetyInputGuardrail;
import com.lushihao.aicode.ai.tools.*;
import com.lushihao.aicode.exception.BusinessException;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.model.enums.CodeGenTypeEnum;
import com.lushihao.aicode.service.ChatHistoryOriginalService;
import com.lushihao.aicode.service.ChatHistoryService;
import com.lushihao.aicode.utils.SpringContextUtil;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * AI 服务创建工厂
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-01   22:40
 */
@Configuration
@Slf4j
public class AiCodeGeneratorServiceFactory {

    @Resource(name = "openAiChatModel")
    private ChatModel chatModel;
    @Resource
    private RedisChatMemoryStore redisChatMemoryStore;
    @Resource
    private ChatHistoryService chatHistoryService;
    @Resource
    private ToolManager toolManager;
    @Resource
    private ChatHistoryOriginalService chatHistoryOriginalService;

    /**
     * AI 服务实例缓存
     * 缓存策略：
     * - 最大缓存 1000 个实例
     * - 写入后 30 分钟过期
     * - 访问后 10 分钟过期
     */
    private final Cache<String, AiCodeGeneratorService> serviceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener((key, value, cause) -> {
                log.debug("AI 服务实例被移除，缓存key: {}, 原因: {}", key, cause);
            })
            .build();

    /**
     * 根据appId 创建AI 代码生成器服务
     *
     * @param appId
     * @return
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId) {
        return getAiCodeGeneratorService(appId, CodeGenTypeEnum.HTML);
    }


    /**
     * 根据appId 和代码生成类型创建AI代码生成器服务
     * @param appId
     * @param codeGenTypeEnum
     * @return
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        // 1.构建缓存的key 确保不同的参数对应不同的缓存值
        String cacheKey = buildCacheKey(appId, codeGenTypeEnum);
        // 2.从缓存中获取AI服务实例 如果缓存中没有就调用createAiCodeGeneratorService创建一个新的实例
        return serviceCache.get(cacheKey, key -> createAiCodeGeneratorService(appId, codeGenTypeEnum));
    }

    /**
     * 创建新的 AI 服务实例
     */
    private AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        log.info("为 appId: {} 创建新的 AI 服务实例", appId);
        // 根据 appId 构建独立的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(50)
                .build();


        // 根据不同的代码生成类型采用不同的模型配置 如果是HTML或者是MULTI_FILE较为简单的直接采用默认模型 如果是VUE_PROJECT则需要采用推理模型
        return switch (codeGenTypeEnum) {
            case HTML, MULTI_FILE -> {
                // 从数据库加载历史对话到记忆中
                chatHistoryService.loadChatHistoryToMemory(appId, chatMemory, 20);
                StreamingChatModel openAistreamingChatModel = SpringContextUtil.getBean("streamingChatModelPrototype", StreamingChatModel.class);
                yield AiServices.builder(AiCodeGeneratorService.class)
                        .streamingChatModel(openAistreamingChatModel)
                        .chatMemory(chatMemory)
                        // 添加输入护轨
                        .inputGuardrails(new PromptSafetyInputGuardrail())
                        // 添加输出护轨（为了保持流式输出 去除）
//                        .outputGuardrails(new RetryOutputGuardrail())
                        .build();
            }
            case VUE_PROJECT -> {
                chatHistoryOriginalService.loadOriginalChatHistoryToMemory(appId, chatMemory, 50);
                // 1.获取模型的Bean实例
                StreamingChatModel reasoningStreamingChatModel = SpringContextUtil.getBean("reasoningStreamingChatModelPrototype", StreamingChatModel.class);

                yield AiServices.builder(AiCodeGeneratorService.class)
                        .streamingChatModel(reasoningStreamingChatModel)
                        .chatMemoryProvider(memoryId -> chatMemory)
                        .tools(
                                (Object[]) toolManager.getAllTools()
                        )
                        // 当AI调用不存在的工具如何处理
                        .hallucinatedToolNameStrategy(
                                toolExecutionRequest -> ToolExecutionResultMessage.
                                        from(toolExecutionRequest, ":Error:there is not tool called" + toolExecutionRequest.name()))
                        // 最多连续调用20次工具
                        .maxSequentialToolsInvocations(20)
                        // 添加输入护轨
                        .inputGuardrails(new PromptSafetyInputGuardrail())
                        // 添加输出护轨
//                        .outputGuardrails(new RetryOutputGuardrail())
                        .build();

            }
            default ->
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型" + codeGenTypeEnum.getValue());
        };
    }

    /**
     * 构建缓存key
     *
     * @param appId
     * @param codeGenTypeEnum
     * @return
     */
    private String buildCacheKey(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        return appId + "_" + codeGenTypeEnum.getValue();
    }
}
