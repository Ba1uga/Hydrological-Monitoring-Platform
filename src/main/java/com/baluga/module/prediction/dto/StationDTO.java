package com.baluga.module.prediction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationDTO {
    private Integer id;
    private String code;
    private String name;
    private String type;
    private String location;
    private String river;
    private Double longitude;
    private Double latitude;
    private Map<String, Object> waterQuality;
    private String qualityClass;
    private String status;
    private String trend;
    private List<String> tags;
    private String icon;
    private String color;
}

