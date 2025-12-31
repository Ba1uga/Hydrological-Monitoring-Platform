package com.baluga.module.waterproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.baluga.module.waterproject.entity.MajorRiver;

@Repository
public interface MajorRiverRepository extends JpaRepository<MajorRiver, Long> {
}
