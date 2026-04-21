package com.baluga.frontend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class FloodControlQueryTimeRangeGuardTest {

    private static final Path OVERVIEW_CONTROLLER = Path.of(
            "src/main/java/com/baluga/module/floodcontrol/controller/OverviewController.java");
    private static final Path SERVICE_INTERFACE = Path.of(
            "src/main/java/com/baluga/module/floodcontrol/service/MonitoringStationService.java");
    private static final Path QUERY_TIME_CONTEXT = Path.of(
            "src/main/resources/static/floodcontrol/JS/state/queryTimeContext.js");

    @Test
    void overviewController_exposesQueryTimeRangeEndpoint() throws IOException {
        String source = Files.readString(OVERVIEW_CONTROLLER, StandardCharsets.UTF_8);

        assertTrue(source.contains("@GetMapping(\"/queryTimeRange\")"));
        assertTrue(source.contains("getQueryTimeRange()"));
    }

    @Test
    void monitoringStationService_exposesAvailableQueryTimeRangeContract() throws IOException {
        String source = Files.readString(SERVICE_INTERFACE, StandardCharsets.UTF_8);

        assertTrue(source.contains("Map<String, LocalDateTime> getQueryTimeRange();"));
    }

    @Test
    void queryTimeContext_clampsOutOfRangeSelectionsToNearestAvailableHour() throws IOException {
        String source = Files.readString(QUERY_TIME_CONTEXT, StandardCharsets.UTF_8);

        assertTrue(source.contains("/currentOverview/queryTimeRange"));
        assertTrue(source.contains("function clampToAvailableRange"));
        assertTrue(source.contains("已切换到最近可用时点"));
    }
}
