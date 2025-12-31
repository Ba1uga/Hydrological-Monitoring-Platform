package com.baluga.module.prediction.controller;

import com.baluga.common.Result;
import com.baluga.module.prediction.dto.StatsDTO;
import com.baluga.module.prediction.entity.Station;
import com.baluga.module.prediction.service.DataService;
import com.baluga.module.prediction.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prediction/stations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class StationController {

    private final StationService stationService;
    private final DataService dataService;

    @GetMapping("/frontend/all")
    public Result<List<Map<String, Object>>> getAllStationsForFrontend() {
        try {
            List<Station> stations = stationService.getAllStations();
            List<Map<String, Object>> result = stations.stream()
                    .map(stationService::convertStationToMap)
                    .collect(Collectors.toList());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取站点数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getStationById(@PathVariable Integer id) {
        try {
            Station station = stationService.getStationById(id);
            if (station == null) {
                return Result.error("站点不存在");
            }

            Map<String, Object> result = stationService.convertStationToMap(station);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取站点详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/code/{code}")
    public Result<Map<String, Object>> getStationByCode(@PathVariable String code) {
        try {
            Station station = stationService.getStationByCode(code);
            if (station == null) {
                return Result.error("站点不存在");
            }

            Map<String, Object> result = stationService.convertStationToMap(station);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取站点详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/frontend/stats")
    public Result<Map<String, Object>> getFrontendStats() {
        try {
            StatsDTO stats = stationService.getStatistics();
            Map<String, Object> result = new HashMap<>();

            result.put("total", stats.getTotalStations());
            result.put("totalStations", stats.getTotalStations());
            result.put("normalCount", stats.getNormalStations());
            result.put("warningCount", stats.getWarningStations());
            result.put("criticalCount", stats.getCriticalStations());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取统计数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/frontend/stats/map")
    public Result<Map<String, Object>> getMapStats() {
        try {
            StatsDTO stats = stationService.getStatistics();
            Map<String, Object> result = new HashMap<>();

            result.put("totalStations", stats.getTotalStations());
            result.put("normalStations", stats.getNormalStations());
            result.put("warningStations", stats.getWarningStations());
            result.put("criticalStations", stats.getCriticalStations());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取地图统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/frontend/stats/realtime")
    public Result<Map<String, Object>> getRealTimeStats() {
        try {
            StatsDTO stats = stationService.getStatistics();
            Map<String, Object> result = new HashMap<>();

            result.put("totalStations", stats.getTotalStations());
            result.put("normalStations", stats.getNormalStations());
            result.put("warningStations", stats.getWarningStations());
            result.put("criticalStations", stats.getCriticalStations());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取实时统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/summary")
    public Result<Map<String, Object>> getSummary() {
        try {
            StatsDTO stats = stationService.getStatistics();
            Map<String, Object> result = new HashMap<>();

            result.put("total", stats.getTotalStations());
            result.put("normalCount", stats.getNormalStations());
            result.put("warningCount", stats.getWarningStations());
            result.put("criticalCount", stats.getCriticalStations());

            // 添加水质类别分布
            Map<String, Long> qualityDistribution = new HashMap<>();
            qualityDistribution.put("I类", stats.getQualityClassI());
            qualityDistribution.put("II类", stats.getQualityClassII());
            qualityDistribution.put("III类", stats.getQualityClassIII());
            qualityDistribution.put("IV类", stats.getQualityClassIV());
            qualityDistribution.put("V类", stats.getQualityClassV());
            result.put("qualityDistribution", qualityDistribution);

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取汇总数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/history")
    public Result<Map<String, Object>> getStationHistory(@PathVariable Integer id,
                                                              @RequestParam(required = false) Integer days) {
        try {
            Map<String, Object> chartData = dataService.getChartData(id, days != null ? days : 10);
            return Result.success(chartData);
        } catch (Exception e) {
            return Result.error("获取历史数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/predict")
    public Result<List<Map<String, Object>>> getStationPrediction(@PathVariable Integer id,
                                                                       @RequestParam(required = false) Integer days) {
        try {
            List<Map<String, Object>> predictions = dataService.getPredictionData(id, days);
            return Result.success(predictions);
        } catch (Exception e) {
            return Result.error("获取预测数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public Result<List<Map<String, Object>>> getStationsByStatus(@PathVariable String status) {
        try {
            List<Station> stations = stationService.getStationsByStatus(status);
            List<Map<String, Object>> result = stations.stream()
                    .map(stationService::convertStationToMap)
                    .collect(Collectors.toList());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("按状态获取站点失败: " + e.getMessage());
        }
    }

    @GetMapping("/quality/{qualityClass}")
    public Result<List<Map<String, Object>>> getStationsByQualityClass(@PathVariable String qualityClass) {
        try {
            List<Station> stations = stationService.getStationsByQualityClass(qualityClass);
            List<Map<String, Object>> result = stations.stream()
                    .map(stationService::convertStationToMap)
                    .collect(Collectors.toList());

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("按水质类别获取站点失败: " + e.getMessage());
        }
    }
}
