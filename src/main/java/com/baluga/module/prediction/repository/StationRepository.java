package com.baluga.module.prediction.repository;

import com.baluga.module.prediction.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {

    Station findByCode(String code);

    List<Station> findByStatus(String status);

    List<Station> findByQualityClass(String qualityClass);

    @Query("SELECT s FROM Station s WHERE s.status IN ('normal', 'warning', 'critical')")
    List<Station> findAllActive();

    @Query("SELECT COUNT(s) FROM Station s WHERE s.status = 'normal'")
    Long countNormalStations();

    @Query("SELECT COUNT(s) FROM Station s WHERE s.status = 'warning'")
    Long countWarningStations();

    @Query("SELECT COUNT(s) FROM Station s WHERE s.status = 'critical'")
    Long countCriticalStations();

    @Query("SELECT COUNT(s) FROM Station s WHERE s.qualityClass = ?1")
    Long countByQualityClass(String qualityClass);
}
