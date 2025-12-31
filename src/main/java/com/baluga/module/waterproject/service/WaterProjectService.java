package com.baluga.module.waterproject.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baluga.module.waterproject.dto.WaterProjectDTO;
import com.baluga.module.waterproject.entity.WaterProject;
import com.baluga.module.waterproject.repository.WaterProjectRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaterProjectService {

    private final WaterProjectRepository waterProjectRepository;
    private final ObjectMapper objectMapper;

    public List<WaterProjectDTO> getAllProjects() {
        return waterProjectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public WaterProjectDTO getProjectById(Long id) {
        return waterProjectRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<WaterProjectDTO> getProjectsByTag(String tag) {
        return waterProjectRepository.findByTagsContaining(tag).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private WaterProjectDTO convertToDTO(WaterProject entity) {
        WaterProjectDTO dto = new WaterProjectDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setLocation(entity.getLocation());
        dto.setRiver(entity.getRiver());
        dto.setBuiltYear(entity.getBuiltYear());
        dto.setCapacity(entity.getCapacity());
        dto.setPower(entity.getPower());
        dto.setHeight(entity.getHeight());
        dto.setLength(entity.getLength());
        dto.setInvestment(entity.getInvestment());
        dto.setAnnualPower(entity.getAnnualPower());
        dto.setDescription(entity.getDescription());
        dto.setImportance(entity.getImportance());
        dto.setColor(entity.getColor());
        dto.setIcon(entity.getIcon());
        dto.setImgUrl(entity.getImgUrl());
        dto.setLargeImgUrl(entity.getLargeImgUrl());

        // 转换坐标
        dto.setCoordinates(new Double[]{entity.getCoordinateLng(), entity.getCoordinateLat()});

        // 转换标签
        try {
            if (entity.getTags() != null && !entity.getTags().isEmpty()) {
                dto.setTags(objectMapper.readValue(entity.getTags(), new TypeReference<List<String>>() {}));
            }
        } catch (JsonProcessingException e) {
            dto.setTags(Arrays.asList(entity.getTags().split(",")));
        }

        // 转换图表数据
        WaterProjectDTO.ChartData chartData = new WaterProjectDTO.ChartData();
        chartData.setCapacity(entity.getChartCapacity());
        chartData.setPower(entity.getChartPower());
        chartData.setHeight(entity.getChartHeight());
        chartData.setLength(entity.getChartLength());
        chartData.setYearRange(entity.getChartYearRange());
        chartData.setImportance(entity.getChartImportance());
        dto.setChartData(chartData);

        return dto;
    }
}
