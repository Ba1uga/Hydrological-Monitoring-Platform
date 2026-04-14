package com.baluga.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class RepositorySanitizationTest {

    private static final Path APPLICATION_YML = Path.of("src/main/resources/application.yml");
    private static final Path README = Path.of("README.md");

    @Test
    void applicationYaml_usesEnvironmentPlaceholders_andDoesNotContainCommittedSecrets() throws IOException {
        String yaml = Files.readString(APPLICATION_YML, StandardCharsets.UTF_8);

        assertTrue(yaml.contains("${DB_URL:jdbc:mysql://localhost:3306/china_hydrology_monitor}"));
        assertTrue(yaml.contains("${DB_USERNAME:change_me}"));
        assertTrue(yaml.contains("${DB_PASSWORD:change_me}"));
        assertTrue(yaml.contains("${REDIS_HOST:localhost}"));
        assertTrue(yaml.contains("${REDIS_PORT:6379}"));
        assertTrue(yaml.contains("${AMAP_KEY:change_me}"));
        assertTrue(yaml.contains("${AMAP_SECURITY_JS_CODE:change_me}"));

        assertFalse(yaml.contains("Tvgai36183"));
        assertFalse(yaml.contains("a6d9aeded24e4453798fb33a0a736050"));
    }

    @Test
    void readme_matchesActualRuntimeFacts() throws IOException {
        String readme = Files.readString(README, StandardCharsets.UTF_8);

        assertTrue(readme.contains("Java 17"));
        assertTrue(readme.contains("Spring Boot 3.4.13"));
        assertTrue(readme.contains("8080"));
        assertTrue(readme.contains("/floodcontrol/Flood_Control_And_Drought_Relief.html"));
        assertTrue(readme.contains("DB_URL"));
        assertTrue(readme.contains("DB_USERNAME"));
        assertTrue(readme.contains("DB_PASSWORD"));
        assertTrue(readme.contains("AMAP_KEY"));
        assertTrue(readme.contains("AMAP_SECURITY_JS_CODE"));
    }
}
