package com.baluga.module.prediction.service;

import com.baluga.module.prediction.entity.MonitoringData;
import com.baluga.module.prediction.repository.MonitoringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataService {

    private final MonitoringDataRepository monitoringDataRepository;

    public List<Map<String, Object>> getLatestDataForAllStations() {
        List<MonitoringData> latestData = monitoringDataRepository.findLatestDataForAllStations();
        return latestData.stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getHistoricalData(Integer stationId, Integer days) {
        HistoricalSelection selection = selectHistoricalData(stationId, normalizeHistoricalDays(days));
        return selection.rows().stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPredictionData(Integer stationId, Integer days) {
        int normalizedDays = normalizePredictionDays(days);
        List<MonitoringData> predictions = monitoringDataRepository.findPredictionsByStationId(stationId, new Date());

        if (predictions.isEmpty()) {
            List<MonitoringData> fallbackPredictions = monitoringDataRepository.findPredictionsByStationId(
                    stationId,
                    new Date(0L)
            );
            int fromIndex = Math.max(fallbackPredictions.size() - normalizedDays, 0);
            predictions = fallbackPredictions.subList(fromIndex, fallbackPredictions.size());
        }

        return predictions.stream()
                .sorted(Comparator.comparing(MonitoringData::getDataTime))
                .limit(normalizedDays)
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getChartData(Integer stationId, Integer days) {
        HistoricalSelection selection = selectHistoricalData(stationId, normalizeHistoricalDays(days));
        List<Map<String, Object>> historicalData = selection.rows().stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());

        List<String> dates = new ArrayList<>();
        List<Double> pH = new ArrayList<>();
        List<Double> dissolvedOxygen = new ArrayList<>();
        List<Double> cod = new ArrayList<>();
        List<Double> waterLevel = new ArrayList<>();

        for (Map<String, Object> data : historicalData) {
            dates.add(String.valueOf(data.get("date")));
            pH.add((Double) data.get("pH"));
            dissolvedOxygen.add((Double) data.get("dissolvedOxygen"));
            cod.add((Double) data.get("cod"));
            waterLevel.add((Double) data.get("waterLevel"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("pH", pH);
        result.put("DO", dissolvedOxygen);
        result.put("COD", cod);
        result.put("level", waterLevel);
        result.put("sourceMode", selection.sourceMode());
        result.put("anchorTime", selection.anchorTime());
        result.put("usedFallback", selection.usedFallback());
        return result;
    }

    private HistoricalSelection selectHistoricalData(Integer stationId, int days) {
        Date endDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        Date startDate = calendar.getTime();

        List<MonitoringData> currentWindowRows = monitoringDataRepository.findByStationIdAndDateRange(
                        stationId,
                        startDate,
                        endDate
                ).stream()
                .filter(data -> !Boolean.TRUE.equals(data.getIsPredicted()))
                .sorted(Comparator.comparing(MonitoringData::getDataTime))
                .collect(Collectors.toList());

        if (!currentWindowRows.isEmpty()) {
            Date anchorTime = currentWindowRows.get(currentWindowRows.size() - 1).getDataTime();
            return new HistoricalSelection(currentWindowRows, "CURRENT_WINDOW", false, anchorTime);
        }

        List<MonitoringData> latestAvailableRows = monitoringDataRepository.findLatestRealDataByStationId(stationId)
                .stream()
                .filter(data -> !Boolean.TRUE.equals(data.getIsPredicted()))
                .limit(days)
                .sorted(Comparator.comparing(MonitoringData::getDataTime))
                .collect(Collectors.toList());

        if (!latestAvailableRows.isEmpty()) {
            Date anchorTime = latestAvailableRows.get(latestAvailableRows.size() - 1).getDataTime();
            return new HistoricalSelection(latestAvailableRows, "LATEST_AVAILABLE", true, anchorTime);
        }

        return new HistoricalSelection(List.of(), "UNAVAILABLE", false, null);
    }

    private Map<String, Object> convertToMap(MonitoringData data) {
        Map<String, Object> map = new HashMap<>();

        map.put("id", data.getId());
        map.put("stationId", data.getStation().getId());
        map.put("dataTime", data.getDataTime());
        map.put("date", formatDate(data.getDataTime()));

        map.put("pH", data.getPH());
        map.put("dissolvedOxygen", data.getDissolvedOxygen());
        map.put("cod", data.getCod());
        map.put("ammoniaNitrogen", data.getAmmoniaNitrogen());
        map.put("waterLevel", data.getWaterLevel());
        map.put("waterTemperature", data.getWaterTemperature());
        map.put("waterQualityCategory", data.getWaterQualityCategory());
        map.put("isPredicted", data.getIsPredicted());

        return map;
    }

    private String formatDate(Date date) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return localDateTime.getMonthValue() + "/" + localDateTime.getDayOfMonth();
    }

    private int normalizeHistoricalDays(Integer days) {
        return days == null || days <= 0 ? 10 : days;
    }

    private int normalizePredictionDays(Integer days) {
        return days == null || days <= 0 ? 7 : days;
    }

    private record HistoricalSelection(
            List<MonitoringData> rows,
            String sourceMode,
            boolean usedFallback,
            Date anchorTime) {
    }
}
