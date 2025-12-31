package com.baluga.module.waterproject.dto;

import java.util.List;

import lombok.Data;

@Data
public class MajorRiverDTO {
    private Long id;
    private String name;
    private List<Double[]> points;
    private String color;
    private Integer width;
}
