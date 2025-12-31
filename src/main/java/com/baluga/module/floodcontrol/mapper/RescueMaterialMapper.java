package com.baluga.module.floodcontrol.mapper;

import com.baluga.module.floodcontrol.pojo.RescueMaterial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RescueMaterialMapper extends BaseMapper<RescueMaterial> {

    /**
     * 根据模式查询物资列表
     * @param modeType FLOOD 或 DROUGHT
     * @return 物资列表
     */
    @Select("SELECT m.* FROM rescue_material m " +
            "LEFT JOIN rescue_material_category c ON m.category_id = c.id " +
            "WHERE c.mode_type = #{modeType} AND m.status = 1")
    List<RescueMaterial> selectByModeType(@Param("modeType") String modeType);
}
