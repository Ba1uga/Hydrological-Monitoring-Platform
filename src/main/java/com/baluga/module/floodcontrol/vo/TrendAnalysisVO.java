package com.baluga.module.floodcontrol.vo;

import lombok.Data;

@Data
public class TrendAnalysisVO {
    private Integer currentCount;
    private Integer yesterdayCount;
    private Integer floodWarningCount;
    private Integer droughtWarningCount;
    private String trendPercentage;
    private String trendDirection;
}

