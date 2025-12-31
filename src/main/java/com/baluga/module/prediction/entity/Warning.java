package com.baluga.module.prediction.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "warnings")
public class Warning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Column(name = "warning_type", nullable = false)
    private String warningType;

    @Column(name = "warning_level", nullable = false)
    private String warningLevel;

    @Column(name = "parameter", nullable = false)
    private String parameter;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "threshold", nullable = false)
    private BigDecimal threshold;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "warning_time", nullable = false)
    private String warningTime;

    @Column(name = "status")
    private String status = "active";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}
