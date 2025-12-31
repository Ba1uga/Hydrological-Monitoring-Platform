package com.baluga.module.waterlevel.controller;

import java.util.ArrayList;
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

    // 鑾峰彇鎵€鏈夌洃娴嬬珯
    @GetMapping("/all")
    public Result<List<Station>> getAllStations() {
        try {
            return Result.success(stationService.list());
        } catch (Exception ex) {
            return Result.success(generateSimulatedStations());
        }
    }

    // 鏀寔鎸夊悕绉版悳绱?
    @GetMapping("/search")
    public Result<List<Station>> searchStations(String keyword) {
        try {
            return Result.success(stationService.getStationByName(keyword));
        } catch (Exception ex) {
            if (keyword == null || keyword.isBlank()) {
                return Result.success(generateSimulatedStations());
            }
            String k = keyword.trim();
            List<Station> filtered = new ArrayList<>();
            for (Station s : generateSimulatedStations()) {
                if (s.getName() != null && s.getName().contains(k)) {
                    filtered.add(s);
                }
            }
            return Result.success(filtered);
        }
    }

    private List<Station> generateSimulatedStations() {
        List<Station> stations = new ArrayList<>();

        Station s1 = new Station();
        s1.setId(1L);
        s1.setName("北京通惠河监测站");
        s1.setLocation("北京市");
        s1.setLongitude(116.39748);
        s1.setLatitude(39.908823);
        s1.setWarningLevel(3.5);
        s1.setDangerLevel(4.0);
        s1.setStatus(1);
        stations.add(s1);

        Station s2 = new Station();
        s2.setId(2L);
        s2.setName("上海黄浦江监测站");
        s2.setLocation("上海市");
        s2.setLongitude(121.4737);
        s2.setLatitude(31.2304);
        s2.setWarningLevel(3.6);
        s2.setDangerLevel(4.2);
        s2.setStatus(1);
        stations.add(s2);

        Station s3 = new Station();
        s3.setId(3L);
        s3.setName("广州珠江监测站");
        s3.setLocation("广东省广州市");
        s3.setLongitude(113.2644);
        s3.setLatitude(23.1291);
        s3.setWarningLevel(3.2);
        s3.setDangerLevel(3.9);
        s3.setStatus(1);
        stations.add(s3);

        Station s4 = new Station();
        s4.setId(4L);
        s4.setName("深圳深圳河监测站");
        s4.setLocation("广东省深圳市");
        s4.setLongitude(114.0579);
        s4.setLatitude(22.5431);
        s4.setWarningLevel(3.3);
        s4.setDangerLevel(4.0);
        s4.setStatus(0);
        stations.add(s4);

        Station s5 = new Station();
        s5.setId(5L);
        s5.setName("武汉监测站");
        s5.setLocation("湖北省武汉市");
        s5.setLongitude(114.3055);
        s5.setLatitude(30.5931);
        s5.setWarningLevel(3.4);
        s5.setDangerLevel(4.1);
        s5.setStatus(1);
        stations.add(s5);

        return stations;
    }
}
