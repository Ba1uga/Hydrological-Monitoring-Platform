package com.baluga.module.floodcontrol.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MonitoringStationVO {
    private Long id;
    private String stationName;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String stationType;
    private BigDecimal currentValue;
    private String valueUnit;
    private LocalDateTime valueRecordTime;
    private BigDecimal alertThreshold;
    private BigDecimal guaranteeThreshold;
    private String floodRisk;
    private String droughtRisk;
    private String primaryMode;
    private String trend;
    private BigDecimal affectedArea;
    private Boolean isActive;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;

    private String trendDirection;
}

