package com.baluga.module.floodcontrol.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class MonitoringStationServiceQueryTimeTest {

    private static final Path OVERVIEW_CONTROLLER = Path.of(
            "src/main/java/com/baluga/module/floodcontrol/controller/OverviewController.java");
    private static final Path SERVICE_INTERFACE = Path.of(
            "src/main/java/com/baluga/module/floodcontrol/service/MonitoringStationService.java");
    private static final Path SERVICE_IMPL = Path.of(
            "src/main/java/com/baluga/module/floodcontrol/service/impl/MonitoringStationServiceImpl.java");

    @Test
    void overviewController_acceptsOptionalQueryTimeForHourEndpoints() throws IOException {
        String source = Files.readString(OVERVIEW_CONTROLLER, StandardCharsets.UTF_8);

        assertTrue(source.contains("@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\") LocalDateTime queryTime"));
        assertTrue(source.contains("getCurrentHourStationVOs(mode, queryTime)"));
        assertTrue(source.contains("getRealTimeCardData(mode, queryTime)"));
        assertTrue(source.contains("getSevenDaysHistory(stationName, mode, queryTime)"));
    }

    @Test
    void monitoringStationService_definesQueryTimeOverloads() throws IOException {
        String source = Files.readString(SERVICE_INTERFACE, StandardCharsets.UTF_8);

        assertTrue(source.contains("List<MonitoringStationVO> getCurrentHourStationVOs(String mode, LocalDateTime queryTime);"));
        assertTrue(source.contains("DashboardCardVO getRealTimeCardData(String mode, LocalDateTime queryTime);"));
        assertTrue(source.contains("List<MonitoringStationHistoryVO> getSevenDaysHistory(String stationName, String mode, LocalDateTime queryTime);"));
    }

    @Test
    void monitoringStationServiceImpl_distinguishesLatestAndHistoricalQueryBehavior() throws IOException {
        String source = Files.readString(SERVICE_IMPL, StandardCharsets.UTF_8);

        assertTrue(source.contains("private LocalDateTime resolveQueryHour(LocalDateTime queryTime)"));
        assertTrue(source.contains("boolean explicitQuery = queryTime != null;"));
        assertTrue(source.contains("if (!explicitQuery && stations.isEmpty())"));
        assertTrue(source.contains("public DashboardCardVO getRealTimeCardData(String mode, LocalDateTime queryTime)"));
        assertTrue(source.contains("public List<MonitoringStationHistoryVO> getSevenDaysHistory(String stationName, String mode, LocalDateTime queryTime)"));
    }
}
