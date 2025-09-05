package com.lushihao.aicode.langgraph4j.ai;

import com.lushihao.aicode.langgraph4j.model.ImageCollectionPlan;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;


/**
 * 图片收集计划服务
 *
 * @author lushihao
 */
public interface ImageCollectionPlanService {

    /**
     * 根据用户提示词分析需要收集的图片类型和参数
     */
    @SystemMessage(fromResource = "prompt/image-collection-plan-system-prompt.txt")
    ImageCollectionPlan planImageCollection(@UserMessage String userPrompt);
}
