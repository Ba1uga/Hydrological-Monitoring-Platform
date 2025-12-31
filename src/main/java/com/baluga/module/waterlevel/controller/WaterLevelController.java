package com.baluga.module.waterlevel.controller;

import com.baluga.module.waterlevel.dto.PredictionParamDTO;
import com.baluga.module.waterlevel.dto.WaterLevelDTO;
import com.baluga.module.waterlevel.entity.Station;
import com.baluga.module.waterlevel.entity.WaterLevel;
import com.baluga.module.waterlevel.service.IWaterLevelService;
import com.baluga.module.waterlevel.util.Result;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController("WaterLevelDataController")
@RequestMapping("/api/waterlevel/data")
public class WaterLevelController {
    @Resource
    private IWaterLevelService waterLevelService;

    /**
     * 鑾峰彇绔欑偣鏈€鏂版按浣?
     */
    @GetMapping("/latest/{stationId}")
    public Result<WaterLevel> getLatestLevel(@PathVariable Long stationId) {
        try {
            WaterLevel latest = waterLevelService.getLatestLevel(stationId);
            if (latest == null) {
                latest = generateSimulatedLatestLevel(stationId);
            }
            return Result.success(latest);
        } catch (Exception ex) {
            return Result.success(generateSimulatedLatestLevel(stationId));
        }
    }

    /**
     * 鑾峰彇绔欑偣鍘嗗彶姘翠綅
     */
    @GetMapping("/history/{stationId}")
    public Result<List<WaterLevel>> getHistoryLevel(
            @PathVariable Long stationId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        try {
            return Result.success(waterLevelService.getHistoryLevel(stationId, startTime, endTime));
        } catch (Exception ex) {
            return Result.success(List.of());
        }
    }

    /**
     * 鑾峰彇鎵€鏈夌珯鐐瑰綋鍓嶆按浣?
     */
    @GetMapping("/current/all")
    public Result<List<WaterLevel>> getAllCurrentLevels() {
        try {
            List<WaterLevel> levels = waterLevelService.getAllCurrentLevels();
            if (levels == null || levels.isEmpty()) {
                return Result.success(generateSimulatedCurrentLevels());
            }
            return Result.success(levels);
        } catch (Exception ex) {
            return Result.success(generateSimulatedCurrentLevels());
        }
    }

    /**
     * 棰勬祴姘翠綅
     */
    @PostMapping("/predict")
    public Result<List<WaterLevel>> predictLevel(@RequestBody PredictionParamDTO param) {
        return Result.success(waterLevelService.predictLevel(param));
    }

    /**
     * 鏂板姘翠綅璁板綍
     */
    @PostMapping("/add")
    public Result<Boolean> addWaterLevel(@RequestBody WaterLevelDTO waterLevelDTO) {
        WaterLevel waterLevel = new WaterLevel();
        waterLevel.setStationId(waterLevelDTO.getStationId());
        waterLevel.setLevel(waterLevelDTO.getLevel());
        waterLevel.setFlowRate(waterLevelDTO.getFlowRate());
        waterLevel.setTemperature(waterLevelDTO.getTemperature());
        waterLevel.setRecordTime(waterLevelDTO.getRecordTime());
        return Result.success(waterLevelService.save(waterLevel));
    }

    /**
     * 鍒犻櫎姘翠綅璁板綍
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteWaterLevel(@PathVariable Long id) {
        return Result.success(waterLevelService.removeById(id));
    }

    private List<WaterLevel> generateSimulatedCurrentLevels() {
        Date now = new Date();
        List<WaterLevel> list = new ArrayList<>();

        list.add(buildSimulatedLevel(1L, "北京通惠河监测站", "北京市", 116.39748, 39.908823, 2.34, 3.5, 4.0, now, 1));
        list.add(buildSimulatedLevel(2L, "上海黄浦江监测站", "上海市", 121.4737, 31.2304, 3.12, 3.6, 4.2, now, 1));
        list.add(buildSimulatedLevel(3L, "广州珠江监测站", "广东省广州市", 113.2644, 23.1291, 1.89, 3.2, 3.9, now, 1));
        list.add(buildSimulatedLevel(4L, "深圳深圳河监测站", "广东省深圳市", 114.0579, 22.5431, 0.0, 3.3, 4.0, now, 0));
        list.add(buildSimulatedLevel(5L, "武汉监测站", "湖北省武汉市", 114.3055, 30.5931, 2.85, 3.4, 4.1, now, 1));

        return list;
    }

    private WaterLevel generateSimulatedLatestLevel(Long stationId) {
        for (WaterLevel wl : generateSimulatedCurrentLevels()) {
            if (wl.getStationId() != null && wl.getStationId().equals(stationId)) {
                return wl;
            }
        }
        WaterLevel wl = new WaterLevel();
        wl.setStationId(stationId);
        wl.setLevel(0.0);
        wl.setFlowRate(0.0);
        wl.setTemperature(0.0);
        wl.setRecordTime(new Date());
        wl.setStatus("离线");
        return wl;
    }

    private WaterLevel buildSimulatedLevel(
            Long stationId,
            String stationName,
            String stationLocation,
            Double longitude,
            Double latitude,
            Double level,
            Double warningLevel,
            Double dangerLevel,
            Date recordTime,
            Integer stationStatus
    ) {
        WaterLevel wl = new WaterLevel();
        wl.setStationId(stationId);
        wl.setLevel(level);
        wl.setFlowRate(Math.max(0.0, Math.round((1.5 + Math.random()) * 100.0) / 100.0));
        wl.setTemperature(Math.round((18.0 + (Math.random() - 0.5) * 6.0) * 10.0) / 10.0);
        wl.setRecordTime(recordTime);

        String status;
        if (stationStatus != null && stationStatus == 0) {
            status = "离线";
        } else if (dangerLevel != null && level != null && level >= dangerLevel) {
            status = "危险";
        } else if (warningLevel != null && level != null && level >= warningLevel) {
            status = "预警";
        } else {
            status = "正常";
        }
        wl.setStatus(status);

        Station station = new Station();
        station.setId(stationId);
        station.setName(stationName);
        station.setLocation(stationLocation);
        station.setLongitude(longitude);
        station.setLatitude(latitude);
        station.setWarningLevel(warningLevel);
        station.setDangerLevel(dangerLevel);
        station.setStatus(stationStatus);
        wl.setStation(station);
        wl.setStationName(stationName);
        wl.setStationLocation(stationLocation);

        return wl;
    }
}
