package com.baluga.module.waterlevel.mapper;

import com.baluga.module.waterlevel.entity.WaterLevel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface WaterLevelMapper extends BaseMapper<WaterLevel> {
    @Select("SELECT * FROM water_level WHERE station_id = #{stationId} AND record_time BETWEEN #{startTime} AND #{endTime} ORDER BY record_time ASC")
    List<WaterLevel> getLevelByStationAndTime(
            @Param("stationId") Long stationId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );

    @Select("SELECT * FROM water_level WHERE station_id = #{stationId} ORDER BY record_time DESC LIMIT 1")
    WaterLevel getLatestLevel(@Param("stationId") Long stationId);

    @Select("SELECT w.* FROM water_level w INNER JOIN (SELECT station_id, MAX(record_time) as max_time FROM water_level GROUP BY station_id) t ON w.station_id = t.station_id AND w.record_time = t.max_time")
    List<WaterLevel> getAllLatestLevels();
}

