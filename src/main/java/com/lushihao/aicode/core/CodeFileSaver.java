//package com.lushihao.aicode.core;
//
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.util.IdUtil;
//import cn.hutool.core.util.StrUtil;
//import com.lushihao.aicode.ai.model.HtmlCodeResult;
//import com.lushihao.aicode.ai.model.MultiFileCodeResult;
//import com.lushihao.aicode.model.enums.CodeGenTypeEnum;
//
//import java.io.File;
//import java.nio.charset.StandardCharsets;
//
///**
// * 代码文件写入工具类
// *
// * @author: lushihao
// * @version: 1.0
// * create:   2025-09-01   23:07
// */
//@Deprecated
//public class CodeFileSaver {
//    /**
//     * 文件保存的根目录
//     */
//    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";
//    /**
//     * 保存HTML代码
//     * @param result 单文件代码生成结果
//     * @return 文件
//     */
//    public static File saveHtmlCode(HtmlCodeResult result){
//        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.HTML.getValue());
//        writeToFile(baseDirPath,"index.html",result.getHtmlCode());
//        return new File(baseDirPath);
//    }
//
//    /**
//     * 保存多文件代码
//     * @param result 多文件代码生成结果
//     * @return 文件
//     */
//    public static File saveMultiFileCode(MultiFileCodeResult result){
//        String baseDirPath = buildUniqueDir(CodeGenTypeEnum.MULTI_FILE.getValue());
//        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
//        writeToFile(baseDirPath, "style.css", result.getCssCode());
//        writeToFile(baseDirPath, "script.js", result.getJsCode());
//        return new File(baseDirPath);
//    }
//
//    /**
//     * 构建唯一目录路径：tmp/code_output/bizType_雪花ID
//     * @param bizType 业务类型 是单文件 还是多文件
//     * @return 唯一路径
//     */
//    private static String buildUniqueDir(String bizType) {
//        String uniqueDirName = StrUtil.format("{}_{}", bizType, IdUtil.getSnowflakeNextIdStr());
//        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
//        FileUtil.mkdir(dirPath);
//        return dirPath;
//    }
//
//
//}
