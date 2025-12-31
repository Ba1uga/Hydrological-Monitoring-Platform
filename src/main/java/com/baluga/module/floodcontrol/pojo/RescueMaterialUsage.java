package com.baluga.module.floodcontrol.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物资使用记录实体类
 */
@Data
@TableName("rescue_material_usage")
public class RescueMaterialUsage {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long materialId;

    private Integer operationType; // 1-入库, 2-出库/调用, 3-维护, 4-报废

    private BigDecimal quantity;

    private String operator;

    private String relatedTaskId;

    private String remark;

    private LocalDateTime operationTime;
}
