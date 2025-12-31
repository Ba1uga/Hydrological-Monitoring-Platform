package com.baluga.module.waterlevel.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class WaterLevelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 绔欑偣ID */
    @NotNull(message = "站点ID不能为空")
    private Long stationId;

    /** 姘댠?鍊?绫? */
    @NotNull(message = "水位值不能为空")
    private Double level;

    /** 娴侀€?m/s) */
    private Double flowRate;

    /** 姘存俯(鈩? */
    private Double temperature;

    /** 璁板綍鏃堕棿 */
    @NotNull(message = "记录时间不能为空")
    private Date recordTime;
}
