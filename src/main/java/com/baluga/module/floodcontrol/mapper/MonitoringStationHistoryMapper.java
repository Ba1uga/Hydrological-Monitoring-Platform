package com.baluga.module.floodcontrol.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baluga.module.floodcontrol.pojo.MonitoringStationHistory;
import com.baluga.module.floodcontrol.vo.MonitoringStationHistoryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface MonitoringStationHistoryMapper extends BaseMapper<MonitoringStationHistory> {

    /**
     * 查询历史数据列表（包含站点信息）
     */
    @Select("<script>" +
            "SELECT h.*, s.station_name, s.station_type, s.primary_mode " +
            "FROM monitoring_station_history h " +
            "LEFT JOIN monitoring_station s ON h.station_id = s.id " +
            "WHERE 1=1 " +
            "<if test='startDate != null'> AND h.record_date &gt;= #{startDate} </if>" +
            "<if test='endDate != null'> AND h.record_date &lt;= #{endDate} </if>" +
            "<if test='stationName != null and stationName != \"\"'> AND s.station_name LIKE CONCAT('%', #{stationName}, '%') </if>" +
            "ORDER BY h.record_date DESC, h.id ASC" +
            "</script>")
    List<MonitoringStationHistoryVO> searchHistory(@Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate, 
                                                  @Param("stationName") String stationName);
    
    /**
     * 统计指定时间和模式的警戒站点数量
     * 条件：record_date 等于指定时间 且 is_warning = 1 且根据模式过滤
     */
    @Select("<script>" +
            "SELECT COUNT(DISTINCT h.station_id) " +
            "FROM monitoring_station_history h " +
            "WHERE h.record_date = #{time} " +
            "AND h.is_warning = 1 " +
            "<if test='mode != null and mode != \"all\"'> AND h.value_unit = #{valueUnit} </if>" +
            "</script>")
    Long countWarningByTime(@Param("time") LocalDateTime time, @Param("mode") String mode, @Param("valueUnit") String valueUnit);
    
    /**
     * 查询指定时间和模式的警戒站点ID列表
     * 条件：record_date 等于指定时间 且 is_warning = 1 且根据模式过滤
     */
    @Select("<script>" +
            "SELECT DISTINCT h.station_id " +
            "FROM monitoring_station_history h " +
            "WHERE h.record_date = #{time} " +
            "AND h.is_warning = 1 " +
            "<if test='mode != null and mode != \"all\"'> AND h.value_unit = #{valueUnit} </if>" +
            "</script>")
    List<Long> getWarningStationIdsByTime(@Param("time") LocalDateTime time, @Param("mode") String mode, @Param("valueUnit") String valueUnit);
}
