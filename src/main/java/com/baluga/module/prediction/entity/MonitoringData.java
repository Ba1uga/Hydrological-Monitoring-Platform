package com.baluga.module.prediction.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "monitoring_data")
public class MonitoringData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "data_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;

    @Column(name = "pH")
    private Double pH;

    @Column(name = "dissolved_oxygen")
    private Double dissolvedOxygen;

    @Column(name = "COD")
    private Double cod;

    @Column(name = "ammonia_nitrogen")
    private Double ammoniaNitrogen;

    @Column(name = "total_phosphorus")
    private Double totalPhosphorus;

    @Column(name = "total_nitrogen")
    private Double totalNitrogen;

    @Column(name = "water_level")
    private Double waterLevel;

    @Column(name = "flow_rate")
    private Double flowRate;

    @Column(name = "water_temperature")
    private Double waterTemperature;

    @Column(name = "water_quality_category")
    private String waterQualityCategory;

    @Column(name = "data_status")
    private String dataStatus = "valid";

    @Column(name = "is_predicted")
    private Boolean isPredicted = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
}
