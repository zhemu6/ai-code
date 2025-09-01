package com.lushihao.aicode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.lushihao.aicode.mapper")
@SpringBootApplication
public class AiCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeApplication.class, args);
    }

}
