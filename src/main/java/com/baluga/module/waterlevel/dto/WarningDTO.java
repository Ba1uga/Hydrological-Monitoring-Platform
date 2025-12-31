package com.baluga.module.waterlevel.dto;

import java.io.Serializable;

import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class WarningDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 绔欑偣ID */
    @NotNull(message = "站点ID不能为空")
    private Long stationId;

    /** 棰勮鏍囬 */
    @NotBlank(message = "预警标题不能为空")
    private String title;

    /** 棰勮鍐呭 */
    private String content;

    /** 棰勈策绛夌骇锛?-涓€鑸紝2-璀﹀憡锛?-涓ラ噸 */
    @Min(value = 1, message = "预警等级不能小于1")
    @NotNull(message = "预警等级不能为空")
    private Integer level;
}
