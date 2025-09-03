package com.lushihao.aicode.service;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 文件下载服务
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-03   15:57
 */
public interface ProjectDownloadService {
    /**
     * 以zip格式下载项目
     * @param projectPath 项目路径
     * @param downloadFileName 项目名称
     * @param response 响应头
     * @return
     */
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);



}
