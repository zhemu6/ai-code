package com.lushihao.aicode.core.parser;

import com.lushihao.aicode.exception.BusinessException;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.model.enums.CodeGenTypeEnum;

/**
 * 代码解析执行器
 * 根据代码生成类型执行相应的解析逻辑
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   12:13
 */
public class CodeParserExecutor {

    private static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();
    private static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();

    /**
     * 根据生成类型 执行代码解析
     *
     * @param codeContent 代码内容
     * @param codeGenType 代码生成类型
     * @return 解析结果 是 HtmlCodeResult 或者是 MultiFileCodeResult
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码生成类型错误");
        };
    }
}
