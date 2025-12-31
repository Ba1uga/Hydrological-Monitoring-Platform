package com.baluga.module.waterlevel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baluga.module.waterlevel.entity.Station;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface StationMapper extends BaseMapper<Station> {
    @Select("SELECT * FROM station WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Station> selectStationByName(@Param("name") String name);

    @Select("SELECT * FROM station WHERE status = 1")
    List<Station> selectOnlineStations();
}
