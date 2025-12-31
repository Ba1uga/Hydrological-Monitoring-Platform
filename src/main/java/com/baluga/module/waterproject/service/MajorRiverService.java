package com.baluga.module.waterproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baluga.module.waterproject.dto.MajorRiverDTO;
import com.baluga.module.waterproject.entity.MajorRiver;
import com.baluga.module.waterproject.repository.MajorRiverRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MajorRiverService {

    private final MajorRiverRepository majorRiverRepository;
    private final ObjectMapper objectMapper;

    public List<MajorRiverDTO> getAllRivers() {
        return majorRiverRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MajorRiverDTO convertToDTO(MajorRiver entity) {
        MajorRiverDTO dto = new MajorRiverDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setColor(entity.getColor());
        dto.setWidth(entity.getWidth());

        // 转换坐标点
        try {
            if (entity.getPoints() != null && !entity.getPoints().isEmpty()) {
                dto.setPoints(objectMapper.readValue(entity.getPoints(), new TypeReference<List<Double[]>>() {}));
            }
        } catch (JsonProcessingException e) {
            dto.setPoints(new ArrayList<>());
        }

        return dto;
    }
}
