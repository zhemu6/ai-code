package com.lushihao.aicode.manager;

import com.lushihao.aicode.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * COC对象存储管理器
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-03   15:46
 */
@Component
@Slf4j
public class CosManager {
    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;

    /**
     * 上传对象
     * @param key 唯一键
     * @param file 文件
     * @return 上传结果
     */
    public PutObjectResult putObject(String key, File file){
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key, file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 上传文件到COS中并且返回URL地址
     * @param key 唯一键 文件的路径
     * @param file 文件
     * @return URL地址
     */
    public String uploadFile(String key, File file){
        PutObjectResult putObjectResult = putObject(key, file);
        // 上传结果不为空
        if(putObjectResult!= null){
            // 拼接可访问的路径
            String url = String.format("%s/%s", cosClientConfig.getHost(), key);
            log.info("文件上传成功:{} ->{}",file.getName(),url);
            return url;
        }else{
            log.info("文件上传失败:{} 返回结果为空",file.getName());
            return null;
        }

    }


}
