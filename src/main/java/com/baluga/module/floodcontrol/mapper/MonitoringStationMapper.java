package com.baluga.module.floodcontrol.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baluga.module.floodcontrol.pojo.MonitoringStation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface MonitoringStationMapper extends BaseMapper<MonitoringStation> {
    /**
     * 根据站点ID列表查询受影响面积总和
     */
    @Select("<script>" +
            "SELECT COALESCE(SUM(affected_area), 0) " +
            "FROM monitoring_station " +
            "WHERE id IN " +
            "<foreach collection='stationIds' item='id' open='(' separator=',' close=')'> #{id} </foreach>" +
            "</script>")
    BigDecimal sumAffectedAreaByStationIds(@Param("stationIds") List<Long> stationIds);

    @Select("<script>" +
            "SELECT COALESCE(SUM(s.affected_area), 0) " +
            "FROM monitoring_station s " +
            "JOIN monitoring_station_history h ON h.station_id = s.id " +
            "WHERE h.record_date = #{time} " +
            "AND h.is_warning = 1 " +
            "<if test='mode != null and mode != \"all\"'> AND h.value_unit = #{valueUnit} </if>" +
            "</script>")
    BigDecimal sumAffectedAreaByWarningTime(@Param("time") LocalDateTime time,
                                           @Param("mode") String mode,
                                           @Param("valueUnit") String valueUnit);
    
    /**
     * 查询指定站点过去7天的历史数据（整点）
     */
    @Select("<script>" +
            "SELECT * " +
            "FROM monitoring_station " +
            "WHERE 1=1 " +
            "<if test='stationName != null and stationName != \"\"'> AND station_name LIKE CONCAT('%', #{stationName}, '%') </if>" +
            "<if test='startDate != null'> AND value_record_time &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND value_record_time &lt;= #{endDate} </if>" +
            "<choose>" +
            "  <when test='mode == \"flood\"'> AND value_unit = 'm' </when>" +
            "  <when test='mode == \"drought\"'> AND value_unit = '%' </when>" +
            "  <otherwise></otherwise>" +
            "</choose>" +
            "ORDER BY value_record_time ASC, id ASC" +
            "</script>")
    List<MonitoringStation> selectHistoryByStationName(@Param("stationName") String stationName, 
                                                      @Param("startDate") LocalDateTime startDate, 
                                                      @Param("endDate") LocalDateTime endDate,
                                                      @Param("mode") String mode);
}
