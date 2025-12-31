package com.baluga.module.waterproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baluga.module.waterproject.entity.WaterProject;

@Repository
public interface WaterProjectRepository extends JpaRepository<WaterProject, Long> {
    List<WaterProject> findByTagsContaining(String tag);
    List<WaterProject> findByType(String type);
}
