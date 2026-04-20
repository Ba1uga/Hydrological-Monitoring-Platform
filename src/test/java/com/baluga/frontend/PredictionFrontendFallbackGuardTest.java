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

    private static boolean containsAny(String html, String... candidates) {
        for (String candidate : candidates) {
            if (html.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

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

        assertTrue(containsAny(html, "中国水质水位预测可视化平台", "涓浗姘磋川姘翠綅棰勬祴鍙鍖栧钩鍙?"));
        assertTrue(containsAny(html, "水利工程", "姘村埄宸ョ▼"));
        assertTrue(containsAny(html, "水位动态监测", "姘翠綅鍔ㄦ€佺洃娴?"));
        assertTrue(containsAny(html, "水位预测", "姘翠綅棰勬祴"));
        assertTrue(containsAny(html, "防汛抗旱", "闃叉睕鎶楁棻"));
        assertTrue(containsAny(html, "河流水位趋势", "娌虫祦姘翠綅瓒嬪娍"));
        assertTrue(containsAny(html, "水质变化对比", "姘磋川鍙樺寲瀵规瘮"));
        assertTrue(containsAny(html, "站点水质趋势对比", "绔欑偣姘磋川瓒嬪娍瀵规瘮"));
        assertTrue(containsAny(html, "水质水位预测分析", "姘磋川姘翠綅棰勬祴鍒嗘瀽"));
        assertTrue(containsAny(html, "预测天数", "棰勬祴澶╂暟"));
        assertTrue(containsAny(html, "运行预测模型", "杩愯棰勬祴妯″瀷"));
        assertTrue(containsAny(html, "污染指标排行", "姹℃煋鎸囨爣鎺掕"));
        assertTrue(containsAny(html, "站点状态分布", "绔欑偣鐘舵€佸垎甯?"));
        assertTrue(containsAny(html, "站点水质雷达图", "绔欑偣姘磋川闆疯揪鍥?"));
        assertTrue(containsAny(html, "实时预警提醒", "瀹炴椂棰勮鎻愰啋"));
        assertTrue(containsAny(html, "未加载到真实站点数据", "鏈姞杞藉埌鐪熷疄绔欑偣鏁版嵁"));
        assertTrue(containsAny(html, "未加载到真实预警数据", "鏈姞杞藉埌鐪熷疄棰勮鏁版嵁"));
        assertTrue(containsAny(html, "真实数据加载失败，请检查后端接口", "鐪熷疄鏁版嵁鍔犺浇澶辫触"));
        assertTrue(containsAny(html, "正在加载数据", "姝ｅ湪鍔犺浇鏁版嵁"));
        assertTrue(containsAny(html, "数据加载完成", "鏁版嵁鍔犺浇瀹屾垚"));
        assertTrue(containsAny(html, "未加载到真实历史/预测数据", "鏈姞杞藉埌鐪熷疄鍘嗗彶/棰勬祴鏁版嵁"));
        assertTrue(containsAny(html, "预测接口执行失败", "棰勬祴鎺ュ彛鎵ц澶辫触"));
        assertTrue(containsAny(html, "部分资源加载失败", "閮ㄥ垎璧勬簮鍔犺浇澶辫触"));
        assertTrue(containsAny(html, "最新可用数据", "鏈€鏂板彲鐢ㄦ暟鎹?"));
        assertTrue(html.contains("async function initApplication()"));
        assertTrue(html.contains("function initializeMap()"));
        assertTrue(html.contains("function initializeChart()"));
        assertTrue(html.contains("async function updateChartData()"));
        assertTrue(html.contains("function renderChart(station, historicalData, predictedData"));
        assertTrue(html.contains("function showNotification(message, type = 'info')"));
    }

    @Test
    void predictionPage_usesReadableLegendAndPopupCopy() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertTrue(containsAny(html, "优", "浼?"));
        assertTrue(containsAny(html, "良", "鑹?"));
        assertTrue(containsAny(html, "中", "涓?"));
        assertTrue(containsAny(html, "差", "宸?"));
        assertTrue(containsAny(html, "劣", "鍔?"));
        assertTrue(containsAny(html, "监测位置", "鐩戞祴浣嶇疆"));
        assertTrue(containsAny(html, "趋势稳定", "瓒嬪娍绋冲畾"));
        assertTrue(containsAny(html, "pH值", "pH鍊?"));
        assertTrue(containsAny(html, "溶解氧", "婧惰В姘?"));
        assertTrue(containsAny(html, "氨氮", "姘ㄦ爱"));
        assertTrue(containsAny(html, "监测站点", "鐩戞祴绔欑偣"));
        assertTrue(containsAny(html, "closeInfoWindow\">×<", "closeInfoWindow\">脳<"));

        int warningCountOccurrences = html.split("id=\\\"warningCount\\\"", -1).length - 1;
        assertTrue(warningCountOccurrences == 1);
    }

    @Test
    void predictionPage_doesNotContainKnownBrokenHeaderOrScriptFragments() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertFalse(html.contains("?/span>"));
        assertFalse(html.contains("?/div>"));
        assertFalse(html.contains("title=\"濠"));
        assertFalse(html.contains("data-title=\"濠"));
        assertFalse(html.contains("title=\"婵"));
        assertFalse(html.contains("data-title=\"婵"));
        assertFalse(html.contains("const API_BASE_URL = '/api/prediction/stations';\n    const WARNING_API_BASE_URL"));
        assertFalse(html.contains("if (item.seriesName === 'pH闂?)"));
        assertFalse(html.contains("markerContent.innerHTML = station.icon || station.name.charAt(0) || '缂?;"));
        assertFalse(html.contains("showNotification('å"));
    }

    @Test
    void predictionPage_matchesCopyLayoutStructure() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        int leftPanelIndex = html.indexOf("<div class=\"left-panel\">");
        int mapPanelIndex = html.indexOf("<div class=\"map-panel\">");
        int chartPanelIndex = html.indexOf("<div class=\"chart-panel\">");
        int rightPanelIndex = html.indexOf("<div class=\"right-panel\">");
        int chartContainerIndex = html.indexOf("id=\"chartContainer\"");
        int leftChart3Index = html.indexOf("id=\"leftChart3Container\"");
        int mapIndex = html.indexOf("id=\"map\"");

        assertTrue(leftPanelIndex >= 0);
        assertTrue(mapPanelIndex > leftPanelIndex);
        assertTrue(leftChart3Index >= 0);
        assertTrue(mapIndex > leftChart3Index);
        assertTrue(chartPanelIndex > mapPanelIndex);
        assertTrue(chartContainerIndex > chartPanelIndex);
        assertTrue(rightPanelIndex > chartContainerIndex);
        assertFalse(html.contains("<div class=\"center-panel\">"));
    }

    @Test
    void predictionPage_usesIndexInspiredLeftTopNavigation() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertTrue(html.contains(".dash-nav {"));
        assertTrue(html.contains("padding: 5px 10px;"));
        assertTrue(html.contains("border-radius: 50px;"));
        assertTrue(containsAny(html, "left: 20px;", "left: 18px;"));
        assertTrue(html.contains("width: 220px;"));
        assertTrue(containsAny(html, ".dash-nav .nav-text {\n            display: none;", ".dash-nav .nav-text {\r\n            display: none;"));
        assertTrue(html.contains("top: 8px;"));
        assertTrue(containsAny(html, "data-title=\"水位预测\"", "data-title=\"姘翠綅棰勬祴\""));
    }

    @Test
    void predictionPage_usesRadarFocusedRightPanelLayout() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertTrue(html.contains("grid-template-rows: minmax(108px, 0.22fr) minmax(108px, 0.22fr) minmax(320px, 0.78fr) minmax(160px, 0.44fr);"));
        assertTrue(html.contains("grid-template-rows: minmax(98px, 0.2fr) minmax(98px, 0.2fr) minmax(280px, 0.72fr) minmax(148px, 0.4fr);"));
        assertFalse(html.contains("grid-template-rows: 118px 118px minmax(320px, 1.48fr) minmax(146px, 0.54fr);"));
    }

    @Test
    void predictionPage_usesFullscreenViewportLayout() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertFalse(html.contains("max-width: 1800px;"));
        assertTrue(containsAny(html, "min-height: 100dvh;", "min-height: 100vh;"));
        assertTrue(containsAny(html, "height: 100dvh;", "height: 100vh;"));
        assertTrue(html.contains("width: 100%;"));
        assertFalse(html.contains("height: calc(100vh - 20px);"));
        assertFalse(html.contains("height: calc(100vh - 124px);"));
    }

    @Test
    void predictionPage_usesFluidDashboardGridAndTwoRowMediumLayout() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertTrue(html.contains("grid-template-columns: minmax(260px, 18vw) minmax(0, 1fr) minmax(260px, 18vw);"));
        assertTrue(html.contains("grid-template-areas:"));
        assertTrue(html.contains("\"left map right\""));
        assertTrue(html.contains("\"left forecast right\""));
        assertTrue(html.contains("@media (max-width: 1599px) and (min-width: 1366px)"));
        assertTrue(html.contains("grid-template-columns: minmax(236px, 17vw) minmax(0, 1fr) minmax(236px, 17vw);"));
        assertTrue(html.contains("@media (max-width: 1365px)"));
        assertTrue(html.contains("\"map forecast\""));
        assertTrue(html.contains("\"left right\""));
        assertFalse(html.contains("grid-template-columns: 320px 1fr 320px;"));
        assertFalse(html.contains("grid-template-rows: 140px 140px 1fr 1fr;"));
    }

    @Test
    void predictionPage_usesHeaderAndPanelAntiClippingRules() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertTrue(containsAny(html, ".header {\n            position: relative;\n            display: flex;", ".header {\r\n            position: relative;\r\n            display: flex;"));
        assertTrue(html.contains("justify-content: center;"));
        assertTrue(html.contains(".header-right {"));
        assertTrue(html.contains("position: absolute;"));
        assertTrue(html.contains("flex-direction: column;"));
        assertTrue(containsAny(html, "right: 18px;", "right: 20px;"));
        assertFalse(html.contains("height: calc(100% - 34px);"));
        assertTrue(html.contains("overflow: visible;"));
    }
}
