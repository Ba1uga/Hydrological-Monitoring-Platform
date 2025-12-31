package com.baluga.module.prediction.service;

import com.baluga.module.prediction.entity.Config;
import com.baluga.module.prediction.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository configRepository;

    public Config getConfigByKey(String key) {
        return configRepository.findByKeyName(key);
    }

    public List<Config> getConfigsByCategory(String category) {
        return configRepository.findByCategory(category);
    }

    public Map<String, String> getAllConfigs() {
        List<Config> configs = configRepository.findAll();
        Map<String, String> result = new HashMap<>();

        for (Config config : configs) {
            result.put(config.getKeyName(), config.getKeyValue());
        }

        return result;
    }

    public Map<String, String> getConfigMapByCategory(String category) {
        List<Config> configs = configRepository.findByCategory(category);
        Map<String, String> result = new HashMap<>();

        for (Config config : configs) {
            result.put(config.getKeyName(), config.getKeyValue());
        }

        return result;
    }
}
