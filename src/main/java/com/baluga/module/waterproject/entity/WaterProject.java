package com.baluga.module.waterproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "water_project")
public class WaterProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 50)
    private String type;
    
    @Column(columnDefinition = "TEXT")
    private String tags;  // JSON格式存储标签数组
    
    @Column(length = 200)
    private String location;

    @Column(length = 100)
    private String river;

    @Column(length = 50)
    private String builtYear;

    @Column(length = 50)
    private String capacity;

    @Column(length = 50)
    private String power;

    @Column(length = 50)
    private String height;

    @Column(length = 50)
    private String length;

    @Column(length = 50)
    private String investment;

    @Column(length = 50)
    private String annualPower;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String importance;

    @Column(length = 20)
    private String color;

    @Column(length = 10)
    private String icon;

    private Double coordinateLng;
    private Double coordinateLat;

    @Column(length = 500)
    private String imgUrl;

    @Column(length = 500)
    private String largeImgUrl;

    // 图表数据
    private Double chartCapacity;
    private Double chartPower;
    private Double chartHeight;
    private Double chartLength;
    private Integer chartYearRange;
    private Integer chartImportance;
}
