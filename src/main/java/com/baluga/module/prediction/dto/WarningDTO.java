package com.baluga.module.prediction.dto;

import lombok.Data;

@Data
public class WarningDTO {
    private Long id;
    private String type;
    private String level;
    private Integer stationId;
    private String stationName;
    private String parameter;
    private String value;
    private String threshold;
    private String time;
    private String description;
    private String status;
}
