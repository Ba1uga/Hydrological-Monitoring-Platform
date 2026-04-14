package com.baluga.module.waterlevel.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baluga.module.waterlevel.dto.PredictionParamDTO;
import com.baluga.module.waterlevel.dto.WaterLevelDTO;
import com.baluga.module.waterlevel.entity.WaterLevel;
import com.baluga.module.waterlevel.service.IWaterLevelService;
import com.baluga.module.waterlevel.util.Result;

import jakarta.annotation.Resource;

@RestController("WaterLevelDataController")
@RequestMapping("/api/waterlevel/data")
public class WaterLevelController {

    @Resource
    private IWaterLevelService waterLevelService;

    @GetMapping("/latest/{stationId}")
    public Result<WaterLevel> getLatestLevel(@PathVariable Long stationId) {
        try {
            WaterLevel latest = waterLevelService.getLatestLevel(stationId);
            if (latest == null) {
                return Result.error("未找到该站点的最新水位数据");
            }
            return Result.success(latest);
        } catch (Exception ex) {
            return Result.error("水位数据加载失败");
        }
    }

    @GetMapping("/history/{stationId}")
    public Result<List<WaterLevel>> getHistoryLevel(
            @PathVariable Long stationId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        try {
            return Result.success(waterLevelService.getHistoryLevel(stationId, startTime, endTime));
        } catch (Exception ex) {
            return Result.error("历史水位数据加载失败");
        }
    }

    @GetMapping("/current/all")
    public Result<List<WaterLevel>> getAllCurrentLevels() {
        try {
            List<WaterLevel> levels = waterLevelService.getAllCurrentLevels();
            return Result.success(levels == null ? List.of() : levels);
        } catch (Exception ex) {
            return Result.error("实时水位数据加载失败");
        }
    }

    @PostMapping("/predict")
    public Result<List<WaterLevel>> predictLevel(@RequestBody PredictionParamDTO param) {
        try {
            return Result.success(waterLevelService.predictLevel(param));
        } catch (Exception ex) {
            return Result.error("水位预测失败");
        }
    }

    @PostMapping("/add")
    public Result<Boolean> addWaterLevel(@RequestBody WaterLevelDTO waterLevelDTO) {
        try {
            WaterLevel waterLevel = new WaterLevel();
            waterLevel.setStationId(waterLevelDTO.getStationId());
            waterLevel.setLevel(waterLevelDTO.getLevel());
            waterLevel.setFlowRate(waterLevelDTO.getFlowRate());
            waterLevel.setTemperature(waterLevelDTO.getTemperature());
            waterLevel.setRecordTime(waterLevelDTO.getRecordTime());
            return Result.success(waterLevelService.save(waterLevel));
        } catch (Exception ex) {
            return Result.error("新增水位数据失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteWaterLevel(@PathVariable Long id) {
        try {
            return Result.success(waterLevelService.removeById(id));
        } catch (Exception ex) {
            return Result.error("删除水位数据失败");
        }
    }
}
