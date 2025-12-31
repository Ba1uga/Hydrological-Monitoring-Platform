package com.baluga.module.prediction.dto;

import lombok.Data;

@Data
public class StatsDTO {
    private Long totalStations;
    private Long normalStations;
    private Long warningStations;
    private Long criticalStations;

    // 质量分布
    private Long qualityClassI;
    private Long qualityClassII;
    private Long qualityClassIII;
    private Long qualityClassIV;
    private Long qualityClassV;
}
