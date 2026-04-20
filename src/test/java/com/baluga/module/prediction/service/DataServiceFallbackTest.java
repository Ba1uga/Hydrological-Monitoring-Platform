package com.baluga.module.prediction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.baluga.module.prediction.entity.MonitoringData;
import com.baluga.module.prediction.entity.Station;
import com.baluga.module.prediction.repository.MonitoringDataRepository;

@ExtendWith(MockitoExtension.class)
class DataServiceFallbackTest {

    @Mock
    private MonitoringDataRepository monitoringDataRepository;

    @InjectMocks
    private DataService dataService;

    @Test
    void getChartData_usesCurrentWindowDataWhenAvailable() {
        Integer stationId = 7;
        Date newest = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
        Date older = Date.from(Instant.now().minus(2, ChronoUnit.DAYS));
        List<MonitoringData> currentWindowRows = List.of(
                monitoringData(stationId, older, false, 7.1, 6.4, 11.2, 21.3),
                monitoringData(stationId, newest, false, 7.2, 6.6, 11.5, 21.7));

        when(monitoringDataRepository.findByStationIdAndDateRange(eq(stationId), any(Date.class), any(Date.class)))
                .thenReturn(currentWindowRows);

        Map<String, Object> chartData = dataService.getChartData(stationId, 5);

        assertEquals(List.of("7.1", "7.2"), stringify(chartData.get("pH")));
        assertEquals("CURRENT_WINDOW", chartData.get("sourceMode"));
        assertEquals(Boolean.FALSE, chartData.get("usedFallback"));
        assertEquals(newest, chartData.get("anchorTime"));
        verify(monitoringDataRepository, never()).findLatestRealDataByStationId(any());
    }

    @Test
    void getChartData_fallsBackToLatestAvailableRealRowsWhenCurrentWindowIsEmpty() {
        Integer stationId = 3;
        Date oldest = Date.from(Instant.parse("2025-12-27T08:00:00Z"));
        Date middle = Date.from(Instant.parse("2025-12-28T08:00:00Z"));
        Date latest = Date.from(Instant.parse("2025-12-29T08:00:00Z"));
        List<MonitoringData> fallbackRows = List.of(
                monitoringData(stationId, latest, false, 7.3, 7.7, 10.6, 12.4),
                monitoringData(stationId, middle, false, 7.2, 7.5, 10.3, 12.2),
                monitoringData(stationId, oldest, false, 7.1, 7.4, 10.1, 12.1));

        when(monitoringDataRepository.findByStationIdAndDateRange(eq(stationId), any(Date.class), any(Date.class)))
                .thenReturn(List.of());
        when(monitoringDataRepository.findLatestRealDataByStationId(stationId)).thenReturn(fallbackRows);

        Map<String, Object> chartData = dataService.getChartData(stationId, 2);

        assertEquals(List.of("12/28", "12/29"), stringify(chartData.get("dates")));
        assertEquals(List.of("7.2", "7.3"), stringify(chartData.get("pH")));
        assertEquals("LATEST_AVAILABLE", chartData.get("sourceMode"));
        assertEquals(Boolean.TRUE, chartData.get("usedFallback"));
        assertEquals(latest, chartData.get("anchorTime"));
    }

    @Test
    void getPredictionData_prefersCurrentFutureWindow() {
        Integer stationId = 5;
        Date firstFuture = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
        Date secondFuture = Date.from(Instant.now().plus(2, ChronoUnit.DAYS));
        List<MonitoringData> currentPredictions = List.of(
                monitoringData(stationId, firstFuture, true, 7.3, 6.0, 14.2, 115.8),
                monitoringData(stationId, secondFuture, true, 7.4, 6.1, 14.4, 115.9));

        when(monitoringDataRepository.findPredictionsByStationId(eq(stationId), any(Date.class)))
                .thenReturn(currentPredictions);

        List<Map<String, Object>> predictions = dataService.getPredictionData(stationId, 2);

        assertEquals(2, predictions.size());
        assertEquals(formatDate(firstFuture), predictions.get(0).get("date"));
        assertEquals(formatDate(secondFuture), predictions.get(1).get("date"));
        verify(monitoringDataRepository).findPredictionsByStationId(eq(stationId), any(Date.class));
    }

    @Test
    void getPredictionData_fallsBackToLatestPredictionBatchWhenFutureWindowIsEmpty() {
        Integer stationId = 2;
        Date d1 = Date.from(Instant.parse("2026-01-02T08:00:00Z"));
        Date d2 = Date.from(Instant.parse("2026-01-03T08:00:00Z"));
        Date d3 = Date.from(Instant.parse("2026-01-04T08:00:00Z"));
        Date d4 = Date.from(Instant.parse("2026-01-05T08:00:00Z"));
        List<MonitoringData> allPredictions = List.of(
                monitoringData(stationId, d1, true, 7.7, 5.1, 18.1, 88.5),
                monitoringData(stationId, d2, true, 7.8, 5.2, 18.2, 88.4),
                monitoringData(stationId, d3, true, 7.9, 5.3, 18.3, 88.6),
                monitoringData(stationId, d4, true, 8.0, 5.4, 18.4, 88.7));

        when(monitoringDataRepository.findPredictionsByStationId(eq(stationId), any(Date.class)))
                .thenAnswer(invocation -> {
                    Date startDate = invocation.getArgument(1);
                    return startDate.getTime() == 0L ? allPredictions : List.of();
                });

        List<Map<String, Object>> predictions = dataService.getPredictionData(stationId, 2);

        assertEquals(2, predictions.size());
        assertEquals(formatDate(d3), predictions.get(0).get("date"));
        assertEquals(formatDate(d4), predictions.get(1).get("date"));
        assertEquals(Boolean.TRUE, predictions.get(0).get("isPredicted"));
    }

    private static MonitoringData monitoringData(
            Integer stationId,
            Date dataTime,
            boolean predicted,
            double pH,
            double dissolvedOxygen,
            double cod,
            double waterLevel) {
        Station station = new Station();
        station.setId(stationId);

        MonitoringData data = new MonitoringData();
        data.setStation(station);
        data.setDataTime(dataTime);
        data.setPH(pH);
        data.setDissolvedOxygen(dissolvedOxygen);
        data.setCod(cod);
        data.setWaterLevel(waterLevel);
        data.setAmmoniaNitrogen(0.3);
        data.setWaterTemperature(18.5);
        data.setWaterQualityCategory("II类");
        data.setIsPredicted(predicted);
        return data;
    }

    private static List<String> stringify(Object value) {
        assertNotNull(value);
        return ((List<?>) value).stream().map(String::valueOf).toList();
    }

    private static String formatDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(java.time.ZoneId.systemDefault()).getMonthValue()
                + "/"
                + instant.atZone(java.time.ZoneId.systemDefault()).getDayOfMonth();
    }
}
