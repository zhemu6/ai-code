package com.lushihao.aicode.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lushihao.aicode.constant.AppConstant;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.exception.ThrowUtils;
import com.lushihao.aicode.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 抽象代码文件保存器 模板方法模式
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   12:19
 */
public abstract class CodeFileSaverTemplate<T>  {

    /**
     * 文件保存的根目录
     */
    protected  static final String FILE_SAVE_ROOT_DIR = AppConstant.CODE_OUTPUT_ROOT_DIR;

    /**
     * 模板方法 保存代码的具体实现
     * @param result HtmlCodeResult 或者是 MultiFileCodeResult
     * @param appId 应用ID
     * @return
     */
    public final File saveCode(T result,Long appId){
        // 1. 验证输入
        validateInput(result);
        // 2. 构建唯一目录
        String baseDirPath = buildUniqueDir(appId);
        // 3. 保存文件
        saveFiles(result, baseDirPath);
        // 4. 返回文件目录对象
        return new File(baseDirPath);
    }

    protected void validateInput(T result) {
        ThrowUtils.throwIf(result==null, ErrorCode.SYSTEM_ERROR, "代码结果对象内容为空");
    }


    /**
     * 构建唯一目录路径：tmp/code_output/bizType_雪花ID
     * @param appId 应用ID
     * @return 唯一路径
     */
    protected   String buildUniqueDir(Long appId) {
        ThrowUtils.throwIf(appId==null, ErrorCode.SYSTEM_ERROR, "应用ID为空");
        // 获取代码生成类型
        String codeType = getCodeType().getValue();
        // 构建唯一目录 其中为生成 类型_雪花算法生成序号
        String uniqueDirName = StrUtil.format("{}_{}", codeType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 写入单个文件
     * @param dirPath 文件路径
     * @param fileName 文件名
     * @param content 内容
     */
    protected  final void writeToFile(String dirPath,String fileName,String content){
        if (StrUtil.isNotBlank(content)) {
            String filePath = dirPath + File.separator + fileName;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }


    /**
     * 获取代码生成类型 是单文件 还是多文件
     * @return
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 保存文件
     * @param result 结果 HtmlFileCodeResult 或者是 MultiFileCodeResult
     * @param baseDirPath 保存文件地址
     */
    protected abstract void saveFiles(T result, String baseDirPath);

}
