package com.baluga.module.waterlevel.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class WaterLevelControllerFallbackTest {

    private static final Path STATION_CONTROLLER = Path.of(
            "src/main/java/com/baluga/module/waterlevel/controller/StationController.java");
    private static final Path WATER_LEVEL_CONTROLLER = Path.of(
            "src/main/java/com/baluga/module/waterlevel/controller/WaterLevelController.java");
    private static final Path WARNING_CONTROLLER = Path.of(
            "src/main/java/com/baluga/module/waterlevel/controller/WarningController.java");

    @Test
    void stationController_doesNotSilentlyFallbackToSimulatedStations() throws IOException {
        String source = Files.readString(STATION_CONTROLLER, StandardCharsets.UTF_8);

        assertFalse(source.contains("return Result.success(generateSimulatedStations())"));
        assertTrue(source.contains("Result.error("));
    }

    @Test
    void waterLevelController_doesNotSilentlyFallbackToSimulatedWaterLevels() throws IOException {
        String source = Files.readString(WATER_LEVEL_CONTROLLER, StandardCharsets.UTF_8);

        assertFalse(source.contains("generateSimulatedLatestLevel"));
        assertFalse(source.contains("generateSimulatedCurrentLevels"));
        assertTrue(source.contains("Result.error("));
    }

    @Test
    void warningController_doesNotReturnEmptySuccessOnFailure() throws IOException {
        String source = Files.readString(WARNING_CONTROLLER, StandardCharsets.UTF_8);

        assertFalse(source.contains("return Result.success(List.of())"));
        assertTrue(source.contains("Result.error("));
    }
}
