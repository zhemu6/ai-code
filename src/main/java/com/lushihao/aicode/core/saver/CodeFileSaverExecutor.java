package com.lushihao.aicode.core.saver;

import com.lushihao.aicode.ai.model.HtmlCodeResult;
import com.lushihao.aicode.ai.model.MultiFileCodeResult;
import com.lushihao.aicode.exception.BusinessException;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 *  代码文件保存执行器
 *  根据生成类型执行相应的保存逻辑
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   12:38
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaverTemplate = new HtmlCodeFileSaverTemplate();
    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaverTemplate = new MultiFileCodeFileSaverTemplate();

    /**
     * 根据生成代码类型执行相应的代码保存逻辑
     * @param codeResult HtmlCodeResult或者MultiFileCodeResult
     * @param codeGenTypeEnum 生成代码类型
     * @return 保存的文件地址
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenTypeEnum){
        return  switch(codeGenTypeEnum){
            case HTML -> htmlCodeFileSaverTemplate.saveCode((HtmlCodeResult) codeResult);
            case MULTI_FILE -> multiFileCodeFileSaverTemplate.saveCode((MultiFileCodeResult) codeResult);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的代码生成类型");
        };
    }

}
