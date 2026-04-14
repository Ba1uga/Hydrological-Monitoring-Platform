package com.baluga.module.waterlevel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baluga.module.waterlevel.entity.Station;
import com.baluga.module.waterlevel.service.IStationService;
import com.baluga.module.waterlevel.util.Result;

import jakarta.annotation.Resource;

@RestController("WaterLevelStationController")
@RequestMapping("/api/waterlevel/station")
public class StationController {

    @Resource(name = "WaterLevelStationService")
    private IStationService stationService;

    @GetMapping("/all")
    public Result<List<Station>> getAllStations() {
        try {
            return Result.success(stationService.list());
        } catch (Exception ex) {
            return Result.error("站点数据加载失败");
        }
    }

    @GetMapping("/search")
    public Result<List<Station>> searchStations(String keyword) {
        try {
            if (keyword == null || keyword.isBlank()) {
                return Result.success(stationService.list());
            }
            return Result.success(stationService.getStationByName(keyword));
        } catch (Exception ex) {
            return Result.error("站点搜索失败");
        }
    }
}
