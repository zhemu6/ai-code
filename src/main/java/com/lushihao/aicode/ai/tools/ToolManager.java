package com.lushihao.aicode.ai.tools;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件管理贡酒
 * 统一管理所有的工具 提供根据名称获取工具的功能
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-04   9:25
 */
@Slf4j
@Component
public class ToolManager {

    /**
     * 工具名称到工具实例的映射
     */
    private final Map<String,BaseTool> toolMap = new HashMap<>();

    /**
     * 自动注入所有的工具
     */
    @Resource
    private BaseTool[] tools;

    /**
     * 初始化所有工具
     */
    @PostConstruct
    public void initTool(){
        for(BaseTool tool : tools){
            toolMap.put(tool.getToolName(),tool);
//            已注册工具: deleteFile -> ❗🕳️删除文件
            log.info("已注册工具: {} -> {}",tool.getToolName(),tool.getDisplayName());
        }
        log.info("工具管理器初始化完成，已注册{}个工具",toolMap.size());
    }

    /**
     * 根据工具名称获取工具实例
     * @param toolName 工具名称
     * @return 工具实例
     */
    public BaseTool getTool(String toolName){
        return toolMap.get(toolName);
    }

    /**
     * 获取所有注册的工具
     * @return  所有注册的工具
     */
    public BaseTool[] getAllTools(){
        return tools;
    }



}
