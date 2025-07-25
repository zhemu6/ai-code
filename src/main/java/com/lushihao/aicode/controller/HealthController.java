package com.lushihao.aicode.controller;

import com.lushihao.aicode.common.BaseResponse;
import com.lushihao.aicode.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-07-25   12:44
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/")
    public BaseResponse<String> healthCheck(){
        return ResultUtils.success("ok!");
    }
}
