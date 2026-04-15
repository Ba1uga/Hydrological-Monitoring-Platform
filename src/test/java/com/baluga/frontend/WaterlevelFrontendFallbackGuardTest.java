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

        assertTrue(html.contains("\u52a8\u6001\u6c34\u4f4d\u76d1\u6d4b\u5e73\u53f0"));
        assertTrue(html.contains("\u5f53\u524d\u6c34\u4f4d"));
        assertTrue(html.contains("\u8b66\u6212"));
        assertTrue(html.contains("\u5371\u9669"));
        assertTrue(html.contains("\u5929\u6c14\u5f71\u54cd\u4e0e\u6c34\u4f4d\u5206\u6790"));
        assertTrue(html.contains("\u641c\u7d22"));
        assertTrue(html.contains("\u6c34\u4f4d\u5360\u6bd4\u5206\u5e03"));
        assertTrue(html.contains("\u9884\u6d4b\u53c2\u6570\u8bbe\u7f6e"));
        assertTrue(html.contains("\u9884\u8b66\u4fe1\u606f"));
        assertTrue(html.contains("\u5386\u53f2\u6c34\u4f4d\u5bf9\u6bd4"));
        assertTrue(html.contains("\u76d1\u6d4b\u533a\u57df"));
        assertTrue(html.contains("\u6c34\u4f4d\u9884\u8b66"));
        assertTrue(html.contains("\u6c34\u4f4d\u5f02\u5e38"));
    }
}
