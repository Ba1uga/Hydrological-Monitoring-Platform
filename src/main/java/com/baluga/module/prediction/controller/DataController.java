package com.baluga.module.prediction.controller;

import com.baluga.common.Result;
import com.baluga.module.prediction.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prediction/data")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class DataController {

    private final DataService dataService;

    @GetMapping("/realtime/all")
    public Result<List<Map<String, Object>>> getRealTimeData() {
        try {
            List<Map<String, Object>> data = dataService.getLatestDataForAllStations();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取实时数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/historical/{stationId}")
    public Result<Map<String, Object>> getHistoricalData(@PathVariable Integer stationId,
                                                              @RequestParam(required = false) Integer days) {
        try {
            Map<String, Object> chartData = dataService.getChartData(stationId, days != null ? days : 10);
            return Result.success(chartData);
        } catch (Exception e) {
            return Result.error("获取历史数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/prediction/{stationId}")
    public Result<List<Map<String, Object>>> getPredictionData(@PathVariable Integer stationId,
                                                                    @RequestParam(required = false) Integer days) {
        try {
            List<Map<String, Object>> predictions = dataService.getPredictionData(stationId, days);
            return Result.success(predictions);
        } catch (Exception e) {
            return Result.error("获取预测数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/prediction/run")
    public Result<Map<String, Object>> runPrediction(@RequestBody Map<String, Object> request) {
        try {
            Integer stationId = (Integer) request.get("stationId");
            Integer days = (Integer) request.get("days");

            if (stationId == null) {
                return Result.error("站点ID不能为空");
            }

            if (days == null || days <= 0) {
                days = 7;
            }

            List<Map<String, Object>> predictions = dataService.getPredictionData(stationId, days);

            Map<String, Object> result = new HashMap<>();
            result.put("stationId", stationId);
            result.put("days", days);
            result.put("predictions", predictions);
            result.put("message", String.format("成功生成未来%d天的预测数据", days));

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("运行预测失败: " + e.getMessage());
        }
    }
}
