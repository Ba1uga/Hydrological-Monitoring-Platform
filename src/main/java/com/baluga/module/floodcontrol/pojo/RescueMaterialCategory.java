package com.baluga.module.floodcontrol.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 救援物资分类实体类
 */
@Data
@TableName("rescue_material_category")
public class RescueMaterialCategory {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long parentId;

    private String modeType; // FLOOD-防汛, DROUGHT-抗旱

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
