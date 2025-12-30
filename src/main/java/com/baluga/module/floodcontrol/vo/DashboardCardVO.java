package com.baluga.module.floodcontrol.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DashboardCardVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer alertStationCount;
    private String trend;
    private String trendDirection;
    private BigDecimal affectedArea;
    private LocalDateTime dataTime;
}
