package com.lushihao.aicode.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.lushihao.aicode.exception.ErrorCode;
import com.lushihao.aicode.exception.ThrowUtils;
import com.lushihao.aicode.manager.CosManager;
import com.lushihao.aicode.service.ScreenshotService;
import com.lushihao.aicode.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-03   15:58
 */
@Slf4j
@Service
public class ScreenshotServiceImpl implements ScreenshotService {
    @Resource
    private CosManager cosManager;
    /**
     * 生成并上传截图
     * @param webUrl 网页地址
     * @return 上传到COS后的地址
     */
    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        // 1. 参数校验
        ThrowUtils.throwIf(webUrl == null, ErrorCode.PARAMS_ERROR,"网页地址不能为空");
        // 2. 生成截图
        String screenshotImagePath = WebScreenshotUtils.saveWebPageScreenShot(webUrl);
        ThrowUtils.throwIf(StrUtil.isBlank(screenshotImagePath ), ErrorCode.PARAMS_ERROR,"本地截图生成失败");
        try {
            String cosUrl = uploadScreenshotToCos(screenshotImagePath);
            ThrowUtils.throwIf(StrUtil.isBlank(cosUrl ), ErrorCode.PARAMS_ERROR,"截图上传COS对象存储失败");
            log.info("截图上传COS对象存储成功:{} -> {}",webUrl, cosUrl);
            return cosUrl;
        }finally {
            cleanupLocalFile(screenshotImagePath);
        }
    }

    /**
     * 上传截图到COS
     * @param screenshotImagePath 截图本地路径
     * @return 上传到COS后的地址 如果失败 返回null
     */
    private String uploadScreenshotToCos(String screenshotImagePath) {
        if(StrUtil.isBlank(screenshotImagePath)){
            return null;
        }
        File file = FileUtil.file(screenshotImagePath);
        if(!file.exists()){
            log.error("截图文件不存在:{}", screenshotImagePath);
            return null;
        }
        // 创建文件存储的key
        // 创建文件名
        String fileName = UUID.randomUUID().toString().substring(0,8) + "_compress.jpg";
        // 根据文件名创建coskey
        String cosKey = generateScreenCosKey(fileName);
        return cosManager.uploadFile(cosKey,file);



    }

    /**
     * 生成截图的对象存储键
     * @param fileName 文件名
     * @return /screenshot/20250731/fileName.jpg
     */
    private String generateScreenCosKey(String fileName) {
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("/screenshot/%s/%s",datePath,fileName);
    }

    /**
     * 清理本地文件
     * @param localFilePath
     */
    private void cleanupLocalFile(String localFilePath) {
        File file = FileUtil.file(localFilePath);
        if(file.exists()){
            File parentFile = file.getParentFile();
            FileUtil.del(parentFile);
            log.info("清理本地文件成功:{}", localFilePath);
        }
    }

}
