package com.baluga.module.waterlevel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Weather implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String location;

    private Double temperature;

    private Double humidity;

    private Double precipitation;

    @TableField("wind_speed")
    private Double windSpeed;

    @TableField("weather_type")
    private String weatherType;

    @TableField("forecast_time")
    private Date forecastTime;

    @TableField("create_time")
    private Date createTime;
}

