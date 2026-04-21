package com.baluga.frontend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class FloodControlTimeSelectorGuardTest {

    private static final Path HTML = Path.of(
            "src/main/resources/static/floodcontrol/Flood_Control_And_Drought_Relief.html");
    private static final Path MODE_SWITCH = Path.of(
            "src/main/resources/static/floodcontrol/JS/ui/modeSwitch.js");
    private static final Path QUERY_TIME = Path.of(
            "src/main/resources/static/floodcontrol/JS/state/queryTimeContext.js");

    @Test
    void floodControlHtml_exposesQueryTimeTriggerAndPanel() throws IOException {
        String html = Files.readString(HTML, StandardCharsets.UTF_8);

        assertTrue(html.contains("id=\"timeSelectorTrigger\""));
        assertTrue(html.contains("id=\"queryTimePanel\""));
        assertTrue(html.contains("CSS/time-selector.css"));
    }

    @Test
    void timeContextModule_exportsLatestAndHistoricalHelpers() throws IOException {
        String source = Files.readString(QUERY_TIME, StandardCharsets.UTF_8);

        assertTrue(source.contains("export function getSelectedQueryTime"));
        assertTrue(source.contains("export function isLatestQueryTime"));
        assertTrue(source.contains("export function buildQueryTimeParams"));
        assertTrue(source.contains("export function renderQueryTimeHeader"));
    }

    @Test
    void modeSwitch_updatesHeaderAsDataTimeInsteadOfRealtimeClock() throws IOException {
        String source = Files.readString(MODE_SWITCH, StandardCharsets.UTF_8);

        assertTrue(source.contains("renderQueryTimeHeader"));
        assertTrue(source.contains("getSelectedQueryTime"));
        assertTrue(source.contains("formatQueryTime"));
    }
}
