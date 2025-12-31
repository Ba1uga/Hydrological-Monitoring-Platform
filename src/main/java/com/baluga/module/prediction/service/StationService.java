package com.baluga.module.prediction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baluga.module.prediction.dto.StatsDTO;
import com.baluga.module.prediction.entity.Station;
import com.baluga.module.prediction.repository.StationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    public List<Station> getAllStations() {
        return stationRepository.findAllActive();
    }

    public Station getStationById(Integer id) {
        return stationRepository.findById(id).orElse(null);
    }

    public Station getStationByCode(String code) {
        return stationRepository.findByCode(code);
    }

    public List<Station> getStationsByStatus(String status) {
        return stationRepository.findByStatus(status);
    }

    public List<Station> getStationsByQualityClass(String qualityClass) {
        return stationRepository.findByQualityClass(qualityClass);
    }

    public StatsDTO getStatistics() {
        StatsDTO stats = new StatsDTO();

        Long total = stationRepository.count();
        Long normal = stationRepository.countNormalStations();
        Long warning = stationRepository.countWarningStations();
        Long critical = stationRepository.countCriticalStations();

        stats.setTotalStations(total);
        stats.setNormalStations(normal != null ? normal : 0L);
        stats.setWarningStations(warning != null ? warning : 0L);
        stats.setCriticalStations(critical != null ? critical : 0L);

        // 计算水质类别分布
        stats.setQualityClassI(stationRepository.countByQualityClass("I类"));
        stats.setQualityClassII(stationRepository.countByQualityClass("II类"));
        stats.setQualityClassIII(stationRepository.countByQualityClass("III类"));
        stats.setQualityClassIV(stationRepository.countByQualityClass("IV类"));
        stats.setQualityClassV(stationRepository.countByQualityClass("V类"));

        return stats;
    }

    public Map<String, Object> convertStationToMap(Station station) {
        Map<String, Object> result = new HashMap<>();

        if (station == null) {
            return result;
        }

        result.put("id", station.getId());
        result.put("name", station.getName());
        result.put("type", station.getType());
        result.put("location", station.getLocation());
        result.put("river", station.getRiver());
        result.put("coordinates", new Double[]{station.getLongitude(), station.getLatitude()});

        if (station.getWaterQuality() != null) {
            result.put("waterQuality", station.getWaterQuality());
        } else {
            Map<String, Object> defaultQuality = new HashMap<>();
            defaultQuality.put("pH", 7.0);
            defaultQuality.put("DO", 5.0);
            defaultQuality.put("COD", 10.0);
            defaultQuality.put("NH3N", 0.5);
            defaultQuality.put("level", 20.0);
            result.put("waterQuality", defaultQuality);
        }

        result.put("qualityClass", station.getQualityClass());
        result.put("color", station.getColor());
        result.put("icon", station.getIcon() != null ? station.getIcon() : station.getName().substring(0, 1));
        result.put("trend", station.getTrend());
        result.put("status", station.getStatus());

        if (station.getTags() != null) {
            result.put("tags", station.getTags());
        } else {
            result.put("tags", List.of(station.getRiver(), station.getLocation()));
        }

        return result;
    }
}
