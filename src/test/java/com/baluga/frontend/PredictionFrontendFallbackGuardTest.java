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

        assertTrue(html.contains("中国水质水位预测可视化平台"));
        assertTrue(html.contains("左一 站点水位概览"));
        assertTrue(html.contains("左二 水质指标分析"));
        assertTrue(html.contains("左三 水质水位趋势对比"));
        assertTrue(html.contains("中部 水质水位预测图"));
        assertTrue(html.contains("预测天数"));
        assertTrue(html.contains("运行"));
        assertTrue(html.contains("右一 重点指标分布"));
        assertTrue(html.contains("右二 水质等级变化"));
        assertTrue(html.contains("右三 水质指标雷达图"));
        assertTrue(html.contains("重点预警信息轮播"));
        assertTrue(html.contains("未加载到真实站点数据"));
        assertTrue(html.contains("未加载到真实预警数据"));
        assertTrue(html.contains("真实数据加载失败，请检查后端接口"));
        assertTrue(html.contains("正在加载数据"));
        assertTrue(html.contains("数据加载完成"));
        assertTrue(html.contains("未加载到真实历史/预测数据"));
        assertTrue(html.contains("预测接口执行失败"));
        assertTrue(html.contains("部分资源加载失败"));
    }
}
