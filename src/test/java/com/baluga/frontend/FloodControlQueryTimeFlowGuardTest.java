package com.baluga.frontend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class FloodControlQueryTimeFlowGuardTest {

    private static final Path MAIN = Path.of("src/main/resources/static/floodcontrol/JS/main.js");
    private static final Path MAP = Path.of("src/main/resources/static/floodcontrol/JS/map/initMap.js");
    private static final Path NUMBERS = Path.of("src/main/resources/static/floodcontrol/JS/effects/numberAnimation.js");
    private static final Path HISTORY = Path.of("src/main/resources/static/floodcontrol/JS/charts/stationHistoryChart.js");
    private static final Path STATIONS = Path.of("src/main/resources/static/floodcontrol/JS/ui/stationList.js");

    @Test
    void main_switchesToHttpModeWhenHistoricalQueryTimeIsActive() throws IOException {
        String source = Files.readString(MAIN, StandardCharsets.UTF_8);

        assertTrue(source.contains("QUERY_TIME_EVENT"));
        assertTrue(source.contains("isLatestQueryTime"));
        assertTrue(source.contains("skip websocket refresh for historical query time"));
    }

    @Test
    void mapAndSummaryRequestsAppendQueryTimeWhenPresent() throws IOException {
        String mapSource = Files.readString(MAP, StandardCharsets.UTF_8);
        String numberSource = Files.readString(NUMBERS, StandardCharsets.UTF_8);

        assertTrue(mapSource.contains("buildQueryTimeParams"));
        assertTrue(numberSource.contains("buildQueryTimeParams"));
        assertTrue(numberSource.contains("queryTimeStatus"));
    }

    @Test
    void stationHistoryRequestUsesSelectedQueryTimeAsAnchor() throws IOException {
        String source = Files.readString(HISTORY, StandardCharsets.UTF_8);

        assertTrue(source.contains("getSelectedQueryTime"));
        assertTrue(source.contains("queryTime"));
    }

    @Test
    void stationListRendersHistoricalEmptyStateMessage() throws IOException {
        String source = Files.readString(STATIONS, StandardCharsets.UTF_8);

        assertTrue(source.contains("该时点暂无数据"));
    }
}
