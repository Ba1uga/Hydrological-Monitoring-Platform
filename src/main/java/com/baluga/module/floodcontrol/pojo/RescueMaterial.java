package com.baluga.module.floodcontrol.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 救援物资实体类
 */
@Data
@TableName("rescue_material")
public class RescueMaterial {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long categoryId;

    private String name;

    private String specification;

    private String unit;

    private BigDecimal totalQuantity;

    private BigDecimal availableQuantity;

    private Integer status; // 1-正常, 0-停用, 2-维护中

    private String location;

    private LocalDateTime lastCheckTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
