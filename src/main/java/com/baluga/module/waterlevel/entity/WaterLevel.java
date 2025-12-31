package com.baluga.module.waterlevel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("water_level")
public class WaterLevel {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long stationId;

    private Double level;

    private Double flowRate;

    private Double temperature;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    private Date createTime;

    private String status;

    private Date updateTime;

    // 娣诲姞鍏宠仈瀛楁锛堜笉鏄犲皠鍒版暟鎹簱锛?
    @TableField(exist = false)
    private Station station;

    // 娣诲姞涓存椂瀛楁鐢ㄤ簬鏄剧ず
    @TableField(exist = false)
    private String stationName;

    @TableField(exist = false)
    private String stationLocation;
}

