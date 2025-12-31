package com.baluga.module.waterlevel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Warning implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 绔欑偣ID */
    @TableField("station_id")
    private Long stationId;

    /** 棰勮鏍囬 */
    private String title;

    /** 棰勮鍐呭 */
    private String content;

    /** 棰勮绛夌骇锛?-涓€鑸紝2-璀﹀憡锛?-涓ラ噸 */
    private Integer level;

    /** 澶勭悊鐘舵€侊細0-鏈鐞嗭紝1-宸插鐞?*/
    private Integer status;

    /** 鍒涘缓鏃堕棿 */
    @TableField("create_time")
    private Date createTime;
}

