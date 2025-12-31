package com.baluga.module.waterlevel.mapper;

import com.baluga.module.waterlevel.entity.Warning;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface WarningMapper extends BaseMapper<Warning> {
    @Select("SELECT * FROM warning WHERE station_id = #{stationId} ORDER BY create_time DESC")
    List<Warning> selectWarningByStationId(@Param("stationId") Long stationId);

    @Select("SELECT * FROM warning WHERE status = 0 ORDER BY create_time DESC")
    List<Warning> selectUnprocessedWarnings();

    @Select("SELECT * FROM warning WHERE level = #{level} ORDER BY create_time DESC")
    List<Warning> selectWarningByLevel(@Param("level") Integer level);
}

