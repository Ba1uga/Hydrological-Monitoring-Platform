package com.baluga.module.floodcontrol.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 监测站每日历史快照实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("monitoring_station_history")
public class MonitoringStationHistory {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long stationId;
    
    private LocalDateTime recordDate;
    
    /**
     * 当日是否警戒(1:是, 0:否)
     */
    private Boolean isWarning;
    
    private String valueUnit;
    
    private BigDecimal currentValue;
    
    private String floodRisk;
    
    private String droughtRisk;
    
    private LocalDateTime createTime;
}
