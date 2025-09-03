package com.lushihao.aicode.service;

/**
 * 截图服务
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-03   15:57
 */
public interface ScreenshotService {
    /**
     * 生成并上传截图
     * @param webUrl 网页地址
     * @return 上传到COS后的地址
     */
    String generateAndUploadScreenshot(String webUrl);
}
