package com.baluga.config;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class RedisCacheConfigTest {

    private static final Path REDIS_CACHE_CONFIG = Path.of(
            "src/main/java/com/baluga/config/RedisCacheConfig.java");

    @Test
    void redisCacheConfig_definesFailOpenCacheErrorHandler() throws IOException {
        String source = Files.readString(REDIS_CACHE_CONFIG, StandardCharsets.UTF_8);

        assertTrue(source.contains("extends CachingConfigurerSupport"));
        assertTrue(source.contains("public CacheErrorHandler errorHandler()"));
        assertTrue(source.contains("handleCacheGetError"));
        assertTrue(source.contains("fallback to method execution"));
        assertTrue(source.contains("handleCachePutError"));
        assertTrue(source.contains("skip cache write"));
    }
}
