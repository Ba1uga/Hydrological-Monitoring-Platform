package com.baluga.module.prediction.repository;

import com.baluga.module.prediction.entity.Warning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Long> {

    List<Warning> findByStationId(Integer stationId);

    List<Warning> findByStatus(String status);

    List<Warning> findByWarningLevel(String warningLevel);

    @Query("SELECT w FROM Warning w WHERE w.status = 'active' ORDER BY " +
            "CASE w.warningLevel WHEN 'serious' THEN 1 WHEN 'warning' THEN 2 WHEN 'caution' THEN 3 ELSE 4 END, " +
            "w.createdAt DESC")
    List<Warning> findActiveWarnings();

    Long countByStatus(String status);
}
