package com.baluga.module.floodcontrol.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.baluga.module.floodcontrol.service.MonitoringStationService;
import com.baluga.module.floodcontrol.vo.DashboardCardVO;
import com.baluga.module.floodcontrol.vo.MonitoringStationVO;

import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket消息处理器
 * 用于处理WebSocket连接和消息，并实现数据推送功能
 */
@Controller
@Slf4j
public class WebSocketHandler {
    
    @Autowired
    private MonitoringStationService monitoringStationService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * 处理客户端发送的消息
     * @param message 客户端发送的消息
     * @return 响应消息
     */
    @MessageMapping("/message")
    @SendTo("/topic/response")
    public String handleMessage(String message) {
        log.info("收到客户端消息: {}", message);
        return "服务器收到消息: " + message;
    }

    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    /**
     * 推送当前整点的站点数据到所有客户端
     * @param mode 模式：all/flood/drought
     */
    public void pushCurrentHourStations(String mode) {
        try {
            // 获取当前整点的站点数据
            java.util.List<MonitoringStationVO> stations = monitoringStationService.getCurrentHourStationVOs(mode);
            // 推送数据到客户端
            messagingTemplate.convertAndSend("/topic/currentHourStations/" + mode, stations);
            log.info("已推送当前整点站点数据到主题: /topic/currentHourStations/{}", mode);
        } catch (Exception e) {
            log.error("推送当前整点站点数据失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 推送仪表盘卡片数据到所有客户端
     * @param mode 模式：all/flood/drought
     */
    public void pushDashboardCardData(String mode) {
        try {
            // 获取仪表盘卡片数据
            DashboardCardVO cardData = monitoringStationService.getDashboardCardData(mode);
            // 推送数据到客户端
            messagingTemplate.convertAndSend("/topic/dashboardCard/" + mode, cardData);
            log.info("已推送仪表盘卡片数据到主题: /topic/dashboardCard/{}", mode);
        } catch (Exception e) {
            log.error("推送仪表盘卡片数据失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 推送实时卡片数据到所有客户端
     * @param mode 模式：all/flood/drought
     */
    public void pushRealTimeCardData(String mode) {
        try {
            // 获取实时卡片数据
            DashboardCardVO cardData = monitoringStationService.getRealTimeCardData(mode);
            // 推送数据到客户端
            messagingTemplate.convertAndSend("/topic/realTimeCard/" + mode, cardData);
            log.info("已推送实时卡片数据到主题: /topic/realTimeCard/{}", mode);
        } catch (Exception e) {
            log.error("推送实时卡片数据失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 推送所有类型的数据到客户端
     */
    public void pushAllData() {
        // 推送不同模式的数据
        pushCurrentHourStations("all");
        pushCurrentHourStations("flood");
        pushCurrentHourStations("drought");
        
        pushDashboardCardData("all");
        pushDashboardCardData("flood");
        pushDashboardCardData("drought");
        
        pushRealTimeCardData("all");
        pushRealTimeCardData("flood");
        pushRealTimeCardData("drought");
    }
}
