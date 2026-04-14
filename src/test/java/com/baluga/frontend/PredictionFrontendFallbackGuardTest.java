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
}
