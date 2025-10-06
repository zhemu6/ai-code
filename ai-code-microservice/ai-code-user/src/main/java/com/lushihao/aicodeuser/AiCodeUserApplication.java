package com.lushihao.aicodeuser;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-10-06   14:13
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.lushihao.aicodeuser.mapper")
@ComponentScan("com.lushihao")
public class AiCodeUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeUserApplication.class, args);
    }
}
