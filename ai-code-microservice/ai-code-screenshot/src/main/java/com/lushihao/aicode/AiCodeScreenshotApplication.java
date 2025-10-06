package com.lushihao.aicode;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-10-06   15:11
 */

@SpringBootApplication
@EnableDubbo
public class AiCodeScreenshotApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeScreenshotApplication.class, args);
    }
}
