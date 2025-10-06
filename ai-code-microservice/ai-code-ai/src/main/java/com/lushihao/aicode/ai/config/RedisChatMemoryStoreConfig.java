package com.lushihao.aicode.ai.config;

import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis持久化对话记忆
 *
 * @author: lushihao
 * @version: 1.0
 * create:   2025-09-02   19:22
 */

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Data
@Slf4j
public class RedisChatMemoryStoreConfig {

    private String host;
    private int port;
    private String password;
    private long ttl;

    @Bean
    public RedisChatMemoryStore redisChatMemoryStore() {
        return RedisChatMemoryStore.builder()
                .host(host)
                .port(port)
                .ttl(ttl)
                .build();
    }
}
