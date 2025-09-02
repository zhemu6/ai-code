package com.lushihao.aicode.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * 生成多个代码文件的结果
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-01   22:52
 */
@Description("生成多个代码文件的结果")
@Data
public class MultiFileCodeResult {
    /**
     * html 代码
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * css 代码
     */
    @Description("CSS代码")
    private String cssCode;

    /**
     * js 代码
     */
    @Description("JS代码")
    private String jsCode;

    /**
     * 代码描述
     */
    @Description("生成代码的描述")
    private String description;
}
