package com.baluga.frontend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class PredictionFrontendFallbackGuardTest {

    private static final Path PAGE = Path.of("src/main/resources/static/prediction/prediction.html");

    @Test
    void predictionPage_doesNotRetainMockBootstrapFunctions() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertFalse(html.contains("useMockData("));
        assertFalse(html.contains("generateMockWarnings("));
        assertFalse(html.contains("generatePredictionData("));
        assertTrue(html.contains("const DATA_API_BASE_URL = '/api/prediction/data';"));
        assertTrue(html.contains("async function loadChartData(stationId, days)"));
    }

    @Test
    void predictionPage_usesReadableUserFacingCopy() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertTrue(html.contains("\u4e2d\u56fd\u6c34\u8d28\u6c34\u4f4d\u9884\u6d4b\u53ef\u89c6\u5316\u5e73\u53f0"));
        assertTrue(html.contains("\u5de6\u4e00 \u7ad9\u70b9\u6c34\u4f4d\u6982\u89c8"));
        assertTrue(html.contains("\u5de6\u4e8c \u6c34\u8d28\u6307\u6807\u5206\u6790"));
        assertTrue(html.contains("\u5de6\u4e09 \u6c34\u8d28\u6c34\u4f4d\u8d8b\u52bf\u5bf9\u6bd4"));
        assertTrue(html.contains("\u4e2d\u90e8 \u6c34\u8d28\u6c34\u4f4d\u9884\u6d4b\u56fe"));
        assertTrue(html.contains("\u9884\u6d4b\u5929\u6570"));
        assertTrue(html.contains("\u8fd0\u884c"));
        assertTrue(html.contains("\u53f3\u4e00 \u91cd\u70b9\u6307\u6807\u5206\u5e03"));
        assertTrue(html.contains("\u53f3\u4e8c \u6c34\u8d28\u7b49\u7ea7\u53d8\u5316"));
        assertTrue(html.contains("\u53f3\u4e09 \u6c34\u8d28\u6307\u6807\u96f7\u8fbe\u56fe"));
        assertTrue(html.contains("\u91cd\u70b9\u9884\u8b66\u4fe1\u606f\u8f6e\u64ad"));
        assertTrue(html.contains("\u672a\u52a0\u8f7d\u5230\u771f\u5b9e\u7ad9\u70b9\u6570\u636e"));
        assertTrue(html.contains("\u672a\u52a0\u8f7d\u5230\u771f\u5b9e\u9884\u8b66\u6570\u636e"));
        assertTrue(html.contains("\u771f\u5b9e\u6570\u636e\u52a0\u8f7d\u5931\u8d25\uff0c\u8bf7\u68c0\u67e5\u540e\u7aef\u63a5\u53e3"));
        assertTrue(html.contains("\u6b63\u5728\u52a0\u8f7d\u6570\u636e"));
        assertTrue(html.contains("\u6570\u636e\u52a0\u8f7d\u5b8c\u6210"));
        assertTrue(html.contains("\u672a\u52a0\u8f7d\u5230\u771f\u5b9e\u5386\u53f2/\u9884\u6d4b\u6570\u636e"));
        assertTrue(html.contains("\u9884\u6d4b\u63a5\u53e3\u6267\u884c\u5931\u8d25"));
        assertTrue(html.contains("\u90e8\u5206\u8d44\u6e90\u52a0\u8f7d\u5931\u8d25"));
    }
}
