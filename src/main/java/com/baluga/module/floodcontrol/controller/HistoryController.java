package com.baluga.module.floodcontrol.controller;

import com.baluga.common.Result;
import com.baluga.module.floodcontrol.service.MonitoringStationService;
import com.baluga.module.floodcontrol.vo.MonitoringStationHistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 历史数据查询控制器
 */
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private MonitoringStationService monitoringStationService;

    /**
     * 查询历史数据
     * @param startDate 开始日期时间 (yyyy-MM-dd HH:mm:ss)
     * @param endDate 结束日期时间 (yyyy-MM-dd HH:mm:ss)
     * @param stationName 站点名称（模糊查询）
     * @return 历史数据列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getHistoryList(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(required = false) String stationName,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "50") int size) {
        
        return Result.success(monitoringStationService.getHistoryPage(startDate, endDate, stationName, page, size));
    }
}
