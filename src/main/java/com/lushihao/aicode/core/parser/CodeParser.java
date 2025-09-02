package com.lushihao.aicode.core.parser;

/**
 * 代码解析器策略接口
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   12:08
 */
public interface CodeParser<T> {

    /**
     * 解析代码内容
     * @param codeContent 给定模型生成的回答 从中提取代码 html css js
     * @return 解析后的结果
     */
    T parseCode(String codeContent);

}
