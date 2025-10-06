package com.lushihao.aicode.service;

import com.lushihao.aicode.model.dto.chathistory.ChatHistoryQueryRequest;
import com.lushihao.aicode.model.entity.User;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.lushihao.aicode.model.entity.ChatHistory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * 对话历史 服务层。
 *
 * @author <a href="https://github.com/zhemu6">ShihaoLu</a>
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * 添加历史对话
     * @param appId appId
     * @param message 对话信息
     * @param messageType 对话类型 ai/user
     * @param userId 用户id
     * @return 是否添加成功
     */
    boolean addChatMessage(Long appId,String message,String messageType,Long userId);

    /**
     * 根据应用id删除历史对话
     * @param appId appId
     * @return 是否删除成功
     */
    boolean deleteByAppId(Long appId);

    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);

    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);
}
