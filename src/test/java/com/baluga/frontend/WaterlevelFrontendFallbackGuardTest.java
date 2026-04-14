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
}
