package com.baluga.module.floodcontrol.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baluga.common.Result;
import com.baluga.module.floodcontrol.pojo.MonitoringStation;
import com.baluga.module.floodcontrol.service.MonitoringStationService;
import com.baluga.module.floodcontrol.vo.DashboardCardVO;
import com.baluga.module.floodcontrol.vo.MonitoringStationHistoryVO;
import com.baluga.module.floodcontrol.vo.MonitoringStationVO;
import com.baluga.module.floodcontrol.vo.TrendAnalysisVO;

/**
 * 概览控制器
 * 提供大屏展示所需的所有接口
 */
@RestController
@RequestMapping("/currentOverview")
public class OverviewController {

    @Autowired
    private MonitoringStationService monitoringStationService;

    /**
     * 获取处于警戒状态的站点列表
     * @param mode 模式：all/flood/drought，默认all
     * @return 统一响应结果（包含站点列表）
     */
    @GetMapping("/warningStations")
    public Result<List<MonitoringStation>> getWarningStations(@RequestParam(required = false, defaultValue = "all") String mode) {
        return Result.success(monitoringStationService.getWarningStations(mode));
    }

    /**
     * 获取趋势分析数据
     * @param mode 模式：all/flood/drought，默认all
     * @return 统一响应结果（包含趋势分析VO）
     */
    @GetMapping("/trend")
    public Result<TrendAnalysisVO> getTrend(@RequestParam(required = false, defaultValue = "all") String mode) {
        return Result.success(monitoringStationService.getTrendAnalysis(mode));
    }

    /**
     * 获取受影响总面积
     * @param mode 模式：all/flood/drought，默认all
     * @return 统一响应结果（包含面积数值）
     */
    @GetMapping("/affectedArea")
    public Result<BigDecimal> getAffectedArea(@RequestParam(required = false, defaultValue = "all") String mode) {
        return Result.success(monitoringStationService.getTotalAffectedArea(mode));
    }
    
    /**
     * 获取所有站点数据（用于地图展示）
     * @param mode 模式：all/flood/drought，默认all
     * @return 统一响应结果（包含站点VO列表）
     */
    @GetMapping("/stations")
    public Result<List<MonitoringStationVO>> getAllStations(@RequestParam(required = false, defaultValue = "all") String mode) {
        return Result.success(monitoringStationService.getAllStationVOs(mode));
    }
    
    /**
     * 获取当前整点的站点数据
     * @param mode 模式：all/flood/drought，默认all
     * @return 统一响应结果（包含站点VO列表）
     */
    @GetMapping("/currentHourStations")
    public Result<List<MonitoringStationVO>> getCurrentHourStations(@RequestParam(required = false, defaultValue = "all") String mode) {
        return Result.success(monitoringStationService.getCurrentHourStationVOs(mode));
    }
    
    /**
     * 获取仪表盘卡片数据
     * @param mode 模式：all/flood/drought，默认all
     * @return 统一响应结果（包含仪表盘卡片数据）
     */
    @GetMapping("/dashboard-card")
    public Result<DashboardCardVO> getDashboardCardData(@RequestParam(required = false, defaultValue = "all") String mode) {
        return Result.success(monitoringStationService.getDashboardCardData(mode));
    }
    
    /**
     * 获取实时卡片数据
     * @param mode 模式：all/flood/drought，默认all
     * @return 统一响应结果（包含实时卡片数据）
     */
    @GetMapping("/realtime-card")
    public Result<DashboardCardVO> getRealTimeCardData(@RequestParam(required = false, defaultValue = "all") String mode) {
        return Result.success(monitoringStationService.getRealTimeCardData(mode));
    }
    
    /**
     * 获取过去七天的站点历史数据
     * @param stationName 站点名称
     * @param mode 模式
     * @return 统一响应结果（包含过去七天的站点历史数据）
     */
    @GetMapping("/sevenDaysHistory")
    public Result<List<MonitoringStationHistoryVO>> getSevenDaysHistory(@RequestParam String stationName, @RequestParam(required = false, defaultValue = "all") String mode) {
        return Result.success(monitoringStationService.getSevenDaysHistory(stationName, mode));
    }
    
}
