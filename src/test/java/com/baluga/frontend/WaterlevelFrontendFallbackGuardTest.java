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

    private static boolean containsAny(String html, String... candidates) {
        for (String candidate : candidates) {
            if (html.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

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

        assertTrue(html.contains("<title>\u52a8\u6001\u6c34\u4f4d\u76d1\u6d4b\u5e73\u53f0</title>"));
        assertTrue(html.contains("data-title=\"\u9996\u9875\" title=\"\u9996\u9875\""));
        assertTrue(html.contains("\u6c34\u5229\u5de5\u7a0b"));
        assertTrue(html.contains("\u6c34\u4f4d\u9884\u6d4b\u6a21\u578b"));
        assertTrue(html.contains("\u9632\u6c5b\u6297\u65f1"));
        assertTrue(html.contains("<div class=\"decoration-title\">\u52a8\u6001\u6c34\u4f4d\u76d1\u6d4b\u5e73\u53f0</div>"));
        assertTrue(html.contains("<div class=\"time-week\" id=\"currentWeek\">\u661f\u671f\u4e00</div>"));
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
        assertTrue(containsAny(
                html,
                "\u6c34\u4f4d\u5f02\u5e38",
                "\\u6c34\\u4f4d\\u5f02\\u5e38"));
        assertTrue(containsAny(
                html,
                "const weekDays = ['\u661f\u671f\u65e5', '\u661f\u671f\u4e00', '\u661f\u671f\u4e8c', '\u661f\u671f\u4e09', '\u661f\u671f\u56db', '\u661f\u671f\u4e94', '\u661f\u671f\u516d'];",
                "const weekDays = ['\\u661f\\u671f\\u65e5', '\\u661f\\u671f\\u4e00', '\\u661f\\u671f\\u4e8c', '\\u661f\\u671f\\u4e09', '\\u661f\\u671f\\u56db', '\\u661f\\u671f\\u4e94', '\\u661f\\u671f\\u516d'];"));
        assertTrue(containsAny(
                html,
                "alertCount.textContent = '0 \u6761';",
                "alertCount.textContent = '0 \\u6761';"));
        assertTrue(containsAny(
                html,
                "\u672a\u52a0\u8f7d\u5230\u771f\u5b9e\u6c34\u4f4d\u3001\u7ad9\u70b9\u548c\u9884\u8b66\u6570\u636e",
                "\\u672a\\u52a0\\u8f7d\\u5230\\u771f\\u5b9e\\u6c34\\u4f4d\\u3001\\u7ad9\\u70b9\\u548c\\u9884\\u8b66\\u6570\\u636e"));
        assertTrue(containsAny(
                html,
                "\u6682\u65e0\u771f\u5b9e\u9884\u8b66\u6570\u636e",
                "\\u6682\\u65e0\\u771f\\u5b9e\\u9884\\u8b66\\u6570\\u636e"));
        assertTrue(containsAny(
                html,
                "\u5f53\u524d\u63a5\u53e3\u672a\u8fd4\u56de\u53ef\u5c55\u793a\u7684\u9884\u8b66\u8bb0\u5f55",
                "\\u5f53\\u524d\\u63a5\\u53e3\\u672a\\u8fd4\\u56de\\u53ef\\u5c55\\u793a\\u7684\\u9884\\u8b66\\u8bb0\\u5f55"));
    }

    @Test
    void waterlevelPage_doesNotContainKnownEncodingCorruptionFragments() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertFalse(html.contains("\u95c2\u509b\u5007\u7eee\u4fa3"));
        assertFalse(html.contains("alertCount.textContent = '0 闂?;"));
        assertFalse(html.contains("const weekDays = ['闂"));
        assertFalse(html.contains("\u93c6\u50a4\u68c2\u9435\u619e\u60cb"));
        assertFalse(html.contains("\u745c\u7248\u6320\u6fa7\u72b3"));
        assertFalse(html.contains("?/span>"));
        assertFalse(html.contains("?/div>"));
        assertFalse(html.contains("title=\"濠"));
        assertFalse(html.contains("data-title=\"濠"));
    }

    @Test
    void waterlevelPage_keepsMainWrapperInsideContainer() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        int containerIndex = html.indexOf("<div class=\"container\">");
        int mainWrapperIndex = html.indexOf("<div class=\"main-wrapper\">");
        int overlayIndex = html.indexOf("<div class=\"overlay\" id=\"overlay\"></div>");

        assertTrue(containerIndex >= 0);
        assertTrue(mainWrapperIndex > containerIndex);
        assertTrue(overlayIndex > mainWrapperIndex);

        assertFalse(html.contains("        </div>\r\n    </div>\r\n\r\n    <div class=\"main-wrapper\">"));
        assertFalse(html.contains("        </div>\n    </div>\n\n    <div class=\"main-wrapper\">"));
        assertTrue(html.contains("            </div>\r\n        </div>\r\n\r\n    <div class=\"main-wrapper\">")
                || html.contains("            </div>\n        </div>\n\n    <div class=\"main-wrapper\">"));
    }

    @Test
    void waterlevelPage_usesAntiClippingViewportHeaderAndDashboardGrid() throws IOException {
        String html = Files.readString(PAGE, StandardCharsets.UTF_8);

        assertTrue(html.contains("min-height: 100dvh;"));
        assertTrue(html.contains("overflow-y: auto;"));
        assertTrue(html.contains("grid-template-columns: minmax(240px, 18vw) minmax(0, 1fr) minmax(180px, 16vw);"));
        assertTrue(html.contains("grid-template-columns: minmax(280px, 22vw) minmax(0, 1.45fr) minmax(340px, 24vw);"));
        assertTrue(html.contains(".user-info {\n        position: relative;")
                || html.contains(".user-info {\r\n        position: relative;"));
        assertFalse(html.contains("height: calc(100vh - 76px);"));
        assertFalse(html.contains("height: calc(100vh - 62px);"));
        assertFalse(html.contains("height: calc(100vh - 55px);"));
        assertFalse(html.contains("min-width: 320px;"));
    }
}
