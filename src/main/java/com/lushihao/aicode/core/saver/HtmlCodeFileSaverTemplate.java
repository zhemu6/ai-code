package com.lushihao.aicode.core.saver;

import cn.hutool.core.util.StrUtil;
import com.lushihao.aicode.ai.model.HtmlCodeResult;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.exception.ThrowUtils;
import com.lushihao.aicode.model.enums.CodeGenTypeEnum;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   12:28
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {
    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        ThrowUtils.throwIf(StrUtil.isBlank(result.getHtmlCode()), ErrorCode.SYSTEM_ERROR, "html代码不能为空");
    }
}
