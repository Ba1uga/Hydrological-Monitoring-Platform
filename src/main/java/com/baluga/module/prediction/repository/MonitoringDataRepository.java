package com.baluga.module.prediction.repository;

import com.baluga.module.prediction.entity.MonitoringData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MonitoringDataRepository extends JpaRepository<MonitoringData, Long> {

    List<MonitoringData> findByStationIdOrderByDataTimeDesc(Integer stationId);

    @Query("SELECT md FROM MonitoringData md WHERE md.station.id = :stationId AND md.isPredicted = false ORDER BY md.dataTime DESC")
    List<MonitoringData> findLatestRealDataByStationId(@Param("stationId") Integer stationId);

    @Query("SELECT md FROM MonitoringData md WHERE md.station.id = :stationId AND md.isPredicted = true AND md.dataTime >= :startDate ORDER BY md.dataTime")
    List<MonitoringData> findPredictionsByStationId(@Param("stationId") Integer stationId, @Param("startDate") Date startDate);

    @Query("SELECT md FROM MonitoringData md WHERE md.station.id = :stationId AND md.dataTime >= :startDate AND md.dataTime <= :endDate ORDER BY md.dataTime")
    List<MonitoringData> findByStationIdAndDateRange(@Param("stationId") Integer stationId,
                                                     @Param("startDate") Date startDate,
                                                     @Param("endDate") Date endDate);

    @Query("SELECT md FROM MonitoringData md WHERE md.isPredicted = false AND md.dataTime = (SELECT MAX(md2.dataTime) FROM MonitoringData md2 WHERE md2.station.id = md.station.id AND md2.isPredicted = false)")
    List<MonitoringData> findLatestDataForAllStations();
}
