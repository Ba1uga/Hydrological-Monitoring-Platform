package com.baluga.module.prediction.service;

import com.baluga.module.prediction.dto.WarningDTO;
import com.baluga.module.prediction.entity.Warning;
import com.baluga.module.prediction.repository.WarningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarningService {

    private final WarningRepository warningRepository;

    public List<Warning> getAllWarnings() {
        return warningRepository.findAll();
    }

    public List<Warning> getActiveWarnings() {
        return warningRepository.findActiveWarnings();
    }

    public List<Warning> getWarningsByStation(Integer stationId) {
        return warningRepository.findByStationId(stationId);
    }

    public List<WarningDTO> getWarningDTOs() {
        List<Warning> warnings = warningRepository.findActiveWarnings();
        return warnings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Long getActiveWarningCount() {
        return warningRepository.countByStatus("active");
    }

    private WarningDTO convertToDTO(Warning warning) {
        WarningDTO dto = new WarningDTO();

        dto.setId(warning.getId());
        dto.setType(warning.getWarningType());
        dto.setLevel(warning.getWarningLevel());
        dto.setStationId(warning.getStation().getId());
        dto.setStationName(warning.getStation().getName());
        dto.setParameter(warning.getParameter());
        dto.setValue(warning.getValue().toString());
        dto.setThreshold(warning.getThreshold().toString());
        dto.setTime(warning.getWarningTime());
        dto.setDescription(warning.getDescription());
        dto.setStatus(warning.getStatus());

        return dto;
    }
}
