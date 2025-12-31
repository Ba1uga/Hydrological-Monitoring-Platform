package com.baluga.module.floodcontrol.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MonitoringStationHistoryVO {
    private Long id;
    private Long stationId;
    private LocalDateTime recordDate;
    private Boolean isWarning;
    private String valueUnit;
    private BigDecimal currentValue;
    private String floodRisk;
    private String droughtRisk;
    private LocalDateTime createTime;

    private String stationName;
    private String stationType;
    private String primaryMode;
}

