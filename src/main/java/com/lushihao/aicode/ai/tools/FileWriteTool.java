package com.lushihao.aicode.ai.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import com.lushihao.aicode.constant.AppConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 写入文件工具
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-03   12:01
 */
@Slf4j
@Component
public class FileWriteTool extends BaseTool{
    /**
     * 文件写入
     * @param relativeFilePath 相对路径
     * @param content 文件内容
     * @param appId 从对话上下文记忆中获取appId
     * @return
     */
    @Tool("写入文件到指定路径中")
    public String writeFile(@P("文件的相对路径") String relativeFilePath, @P("文件内容") String content, @ToolMemoryId Long appId) {
        try {
            Path path = Paths.get(relativeFilePath);
            if (!path.isAbsolute()) {
                // 相对路径处理，创建基于 appId 的项目目录
                String projectDirName = "vue_project_" + appId;
                Path projectRoot = Paths.get(AppConstant.CODE_OUTPUT_ROOT_DIR, projectDirName);
                path = projectRoot.resolve(relativeFilePath);
            }
            // 创建父目录（如果不存在）
            Path parentDir = path.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            // 写入文件内容
            Files.write(path, content.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            log.info("成功写入文件: {}", path.toAbsolutePath());
            // 注意要返回相对路径，不能让 AI 把文件绝对路径返回给用户
            return "文件写入成功: " + relativeFilePath;
        } catch (IOException e) {
            String errorMessage = "文件写入失败: " + relativeFilePath + ", 错误: " + e.getMessage();
            log.error(errorMessage, e);
            return errorMessage;
        }
    }

    @Override
    public String getToolName() {
        return "writeFile";
    }

    @Override
    public String getDisplayName() {
        return  "✍️文件写入📄";
    }

    @Override
    public String generateToolExecutedResult(JSONObject arguments) {
            String relativeFilePath = arguments.getStr("relativeFilePath");
            String suffix = FileUtil.getSuffix(relativeFilePath);
            String content = arguments.getStr("content");
            return String.format("""
                        [🔧工具调用] %s %s
                        ```%s
                        %s
                        ```
                        """, getDisplayName(), relativeFilePath, suffix, content);
        }
}

