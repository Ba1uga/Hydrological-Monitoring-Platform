package com.baluga.module.prediction.repository;

import com.baluga.module.prediction.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Integer> {

    Config findByKeyName(String keyName);

    List<Config> findByCategory(String category);
}
