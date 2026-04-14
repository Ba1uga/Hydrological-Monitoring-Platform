package com.baluga.frontend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class WaterlevelFrontendFallbackGuardTest {

    private static final Path PAGE = Path.of("src/main/resources/static/waterlevel/waterlevel.html");

    @Test
    void waterlevelPage_doesNotRetainSimulationFallbackEntryPoints() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertFalse(html.contains("simulateRealTimeData("));
        assertFalse(html.contains("updateWaterLevelDataWithSimulation("));
        assertFalse(html.contains("generateSimulatedWaterData("));
        assertTrue(html.contains("function renderNoDataState(message)"));
        assertTrue(html.contains("function fetchJson(url)"));
    }

    @Test
    void waterlevelPage_usesReadableUserFacingCopy() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertTrue(html.contains("动态水位监测平台"));
        assertTrue(html.contains("当前水位"));
        assertTrue(html.contains("警戒"));
        assertTrue(html.contains("危险"));
        assertTrue(html.contains("天气影响与水位分析"));
        assertTrue(html.contains("搜索"));
        assertTrue(html.contains("水位占比分布"));
        assertTrue(html.contains("预测参数设置"));
        assertTrue(html.contains("预警信息"));
        assertTrue(html.contains("历史水位对比"));
        assertTrue(html.contains("监测区域"));
        assertTrue(html.contains("水位预警"));
        assertTrue(html.contains("水位异常"));
    }
}
