package com.baluga.module.floodcontrol.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DashboardCardVO {
    private Integer alertStationCount;
    private String trend;
    private String trendDirection;
    private BigDecimal affectedArea;
    private LocalDateTime dataTime;
}

