package com.baluga.module.waterlevel.dto;

import java.io.Serializable;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class PredictionParamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 绔欑偣ID */
    @NotNull(message = "站点ID不能为空")
    private Long stationId;

    /** 棰勬祴鏃堕暱锛堝皬鏃讹級 */
    @Min(value = 1, message = "预测时长不能小于1小时")
    private Integer hours;

    /** 閴囨牱棰戠巼锛堝垎閽燂級 */
    @Min(value = 5, message = "采样频率不能小于5分钟")
    private Integer sampleRate;

    /** 棰勬祴妯″瀷 */
    @NotBlank(message = "预测模型不能为空")
    private String model;
}
