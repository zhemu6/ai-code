package com.lushihao.aicode;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author: lushihao
 * @version: 1.0
 * create:   2025-10-06   14:43
 */
@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})
@MapperScan("com.lushihao.aicode.mapper")
@EnableCaching
@EnableDubbo
public class AiCodeAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodeAppApplication.class, args);
    }
}
