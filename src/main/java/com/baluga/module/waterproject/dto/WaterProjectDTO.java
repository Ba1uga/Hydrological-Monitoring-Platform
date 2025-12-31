package com.baluga.module.waterproject.dto;

import java.util.List;

import lombok.Data;

@Data
public class WaterProjectDTO {
    private Long id;
    private String name;
    private String type;
    private List<String> tags;
    private String location;
    private String river;
    private String builtYear;
    private String capacity;
    private String power;
    private String height;
    private String length;
    private String investment;
    private String annualPower;
    private String description;
    private String importance;
    private String color;
    private String icon;
    private Double[] coordinates;
    private String imgUrl;
    private String largeImgUrl;
    private ChartData chartData;

    @Data
    public static class ChartData {
        private Double capacity;
        private Double power;
        private Double height;
        private Double length;
        private Integer yearRange;
        private Integer importance;
    }
}
