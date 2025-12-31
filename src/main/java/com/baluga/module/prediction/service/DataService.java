package com.baluga.module.prediction.service;

import com.baluga.module.prediction.entity.MonitoringData;
import com.baluga.module.prediction.repository.MonitoringDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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
        if (days == null || days <= 0) {
            days = 10;
        }

        Date endDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        Date startDate = calendar.getTime();

        List<MonitoringData> dataList = monitoringDataRepository.findByStationIdAndDateRange(
                stationId, startDate, endDate);

        return dataList.stream()
                .filter(data -> !data.getIsPredicted())
                .sorted(Comparator.comparing(MonitoringData::getDataTime))
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPredictionData(Integer stationId, Integer days) {
        if (days == null || days <= 0) {
            days = 7;
        }

        Date startDate = new Date();
        List<MonitoringData> predictions = monitoringDataRepository.findPredictionsByStationId(
                stationId, startDate);

        return predictions.stream()
                .limit(days)
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getChartData(Integer stationId, Integer days) {
        Map<String, Object> result = new HashMap<>();

        // 获取历史数据
        List<Map<String, Object>> historicalData = getHistoricalData(stationId, days);

        // 提取各指标数据
        List<String> dates = new ArrayList<>();
        List<Double> pH = new ArrayList<>();
        List<Double> dissolvedOxygen = new ArrayList<>();
        List<Double> cod = new ArrayList<>();
        List<Double> waterLevel = new ArrayList<>();

        for (Map<String, Object> data : historicalData) {
            dates.add(data.get("date").toString());
            pH.add((Double) data.get("pH"));
            dissolvedOxygen.add((Double) data.get("dissolvedOxygen"));
            cod.add((Double) data.get("cod"));
            waterLevel.add((Double) data.get("waterLevel"));
        }

        result.put("dates", dates);
        result.put("pH", pH);
        result.put("DO", dissolvedOxygen);
        result.put("COD", cod);
        result.put("level", waterLevel);

        return result;
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
}
