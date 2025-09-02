package com.lushihao.aicode.core.saver;

import cn.hutool.core.util.StrUtil;
import com.lushihao.aicode.ai.model.MultiFileCodeResult;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.exception.ThrowUtils;
import com.lushihao.aicode.model.enums.CodeGenTypeEnum;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   12:35
 */
public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult>{

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        writeToFile(baseDirPath, "script.js", result.getJsCode());
    }

    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        // 校验代码 其中HTML文件必须存在 css和js代码可以为空
        ThrowUtils.throwIf(StrUtil.isBlank(result.getHtmlCode()), ErrorCode.SYSTEM_ERROR, "html代码不能为空");
    }
}
