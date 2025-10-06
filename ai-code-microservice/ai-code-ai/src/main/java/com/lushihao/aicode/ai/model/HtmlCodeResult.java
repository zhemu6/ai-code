package com.lushihao.aicode.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * 封装HTML 代码生成结果
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-01   22:52
 */
@Description("生成 HTML 代码文件的结果")
@Data
public class HtmlCodeResult {
    /**
     * html 代码
     */
    @Description("HTML代码")
    private String htmlCode;

    /**
     * 代码描述
     */
    @Description("生成代码的描述")
    private String description;
}
