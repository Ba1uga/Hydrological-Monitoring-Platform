package com.baluga.module.floodcontrol.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 监测站点实体类
 * 对应数据库表 monitoring_station
 */
@Data
@TableName("monitoring_station")
public class MonitoringStation implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 站点名称（如：长江武汉站）
     */
    private String stationName;
    
    /**
     * 经度
     */
    private BigDecimal longitude;
    
    /**
     * 纬度
     */
    private BigDecimal latitude;
    
    /**
     * 站点类型：river(河流)、lake(湖泊)、reservoir(水库)、monitoring(常规监测站)
     */
    private String stationType;
    
    /**
     * 当前值（防汛模式：水位m；抗旱模式：土壤湿度% 等）
     */
    private BigDecimal currentValue;
    
    /**
     * 当前值单位（m 或 %），用于前端动态显示
     */
    private String valueUnit;
    
    /**
     * 当前值的记录时间（数据采集时间）
     */
    private LocalDateTime valueRecordTime;
    
    /**
     * 警戒阈值（防汛：超过即警戒；抗旱：低于即旱情）
     */
    private BigDecimal alertThreshold;
    
    /**
     * 保证阈值（更高一级阈值，例如防汛中的保证水位，超过更严重；抗旱中的死水位，低于更严重。可选）
     */
    private BigDecimal guaranteeThreshold;
    
    /**
     * 防汛模式风险等级（extreme, high, medium, low, safe）
     */
    private String floodRisk;
    
    /**
     * 抗旱模式风险等级（extreme, high, medium, low, safe）
     */
    private String droughtRisk;
    
    /**
     * 站点主要模式：flood=主要防汛站点，drought=主要抗旱站点，both=两者均重要
     */
    private String primaryMode;
    
    /**
     * 当前值趋势：up(上升)、down(下降)、stable(平稳)
     */
    private String trend;
    
    /**
     * 该站点进入警戒状态时影响的面积（km²）
     */
    private BigDecimal affectedArea;
    
    /**
     * 是否启用（下线站点可置为false）
     */
    private Boolean isActive;
    
    /**
     * 记录更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 记录创建时间
     */
    private LocalDateTime createTime;
}
