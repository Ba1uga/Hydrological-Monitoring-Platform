package com.baluga.module.floodcontrol.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baluga.module.floodcontrol.controller.WebSocketHandler;
import com.baluga.module.floodcontrol.service.MonitoringStationService;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务配置类
 * 实现每小时整点自动执行数据查询操作
 */
@Component
@EnableScheduling
@ConditionalOnProperty(name = "app.scheduling.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class TaskScheduler {
    
    @Autowired
    private MonitoringStationService monitoringStationService;
    
    @Autowired
    private WebSocketHandler webSocketHandler;
    
    /**
     * 每小时整点执行数据查询
     * 表达式含义：0 0 * * * ? 表示在每分钟的第0秒，每小时的第0分钟执行
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void executeHourlyDataQuery() {
        log.info("开始执行每小时整点数据查询任务");
        
        try {
            // 执行当前整点数据查询
            // 查询所有模式的数据
            monitoringStationService.getCurrentHourStationVOs("all");
            monitoringStationService.getCurrentHourStationVOs("flood");
            monitoringStationService.getCurrentHourStationVOs("drought");
            
            // 查询仪表盘卡片数据
            monitoringStationService.getDashboardCardData("all");
            monitoringStationService.getDashboardCardData("flood");
            monitoringStationService.getDashboardCardData("drought");
            
            // 推送数据到客户端
            webSocketHandler.pushAllData();
            
            log.info("每小时整点数据查询任务执行完成");
        } catch (Exception e) {
            log.error("每小时整点数据查询任务执行失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 启动时执行一次数据查询
     * 确保应用启动后立即获取最新数据
     */
    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    public void executeInitialDataQuery() {
        log.info("开始执行初始化数据查询任务");
        
        try {
            // 执行当前整点数据查询
            monitoringStationService.getCurrentHourStationVOs("all");
            
            // 推送数据到客户端
            webSocketHandler.pushAllData();
            
            log.info("初始化数据查询任务执行完成");
        } catch (Exception e) {
            log.error("初始化数据查询任务执行失败: {}", e.getMessage(), e);
        }
    }
}
