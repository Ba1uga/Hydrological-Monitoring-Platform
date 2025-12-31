package com.baluga.module.prediction.controller;

import com.baluga.common.Result;
import com.baluga.module.prediction.dto.WarningDTO;
import com.baluga.module.prediction.service.WarningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prediction/warnings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class WarningController {

    private final WarningService warningService;

    @GetMapping("/frontend/all")
    public Result<List<WarningDTO>> getAllWarningsForFrontend() {
        try {
            List<WarningDTO> warnings = warningService.getWarningDTOs();
            return Result.success(warnings);
        } catch (Exception e) {
            return Result.error("获取预警数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/frontend/active")
    public Result<List<WarningDTO>> getActiveWarnings() {
        try {
            List<WarningDTO> warnings = warningService.getWarningDTOs();
            return Result.success(warnings);
        } catch (Exception e) {
            return Result.error("获取活跃预警失败: " + e.getMessage());
        }
    }

    @GetMapping("/frontend/stats")
    public Result<Map<String, Object>> getWarningStats() {
        try {
            Long activeCount = warningService.getActiveWarningCount();
            Map<String, Object> result = new HashMap<>();
            result.put("activeCount", activeCount != null ? activeCount : 0);
            result.put("totalCount", activeCount != null ? activeCount : 0);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("获取预警统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/station/{stationId}")
    public Result<List<WarningDTO>> getWarningsByStation(@PathVariable Integer stationId) {
        try {
            List<WarningDTO> warnings = warningService.getWarningsByStation(stationId)
                    .stream()
                    .map(warning -> {
                        WarningDTO dto = new WarningDTO();
                        dto.setId(warning.getId());
                        dto.setType(warning.getWarningType());
                        dto.setLevel(warning.getWarningLevel());
                        dto.setStationId(warning.getStation().getId());
                        dto.setStationName(warning.getStation().getName());
                        dto.setParameter(warning.getParameter());
                        dto.setValue(warning.getValue().toString());
                        dto.setThreshold(warning.getThreshold().toString());
                        dto.setTime(warning.getWarningTime());
                        dto.setDescription(warning.getDescription());
                        dto.setStatus(warning.getStatus());
                        return dto;
                    })
                    .toList();

            return Result.success(warnings);
        } catch (Exception e) {
            return Result.error("获取站点预警失败: " + e.getMessage());
        }
    }
}
