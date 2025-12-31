package com.baluga.module.waterlevel.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("station")
public class Station implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 绔欑偣鍚嶇О */
    private String name;

    /** 绔欑偣浣嶇疆 */
    private String location;

    /** 缁忓害 */
    private Double longitude;

    /** 绾害 */
    private Double latitude;

    /** 璀︽垝姘翠綅 */
    @TableField("warning_level")
    private Double warningLevel;

    /** 鍗遍櫓姘翠綅 */
    @TableField("danger_level")
    private Double dangerLevel;

    /** 鐘舵€侊：0-绂荤嚎锛?-姝⑤父 */
    private Integer status;

    /** 鍒涘缓鏃堕棿 */
    @TableField("create_time")
    private Date createTime;

    /** 鏇存柊鏃堕棿 */
    @TableField("update_time")
    private Date updateTime;
}
