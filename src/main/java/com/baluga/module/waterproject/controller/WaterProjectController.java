package com.baluga.module.waterproject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baluga.module.waterproject.dto.WaterProjectDTO;
import com.baluga.module.waterproject.service.WaterProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class WaterProjectController {

    private final WaterProjectService waterProjectService;

    @GetMapping
    public ResponseEntity<List<WaterProjectDTO>> getAllProjects() {
        return ResponseEntity.ok(waterProjectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterProjectDTO> getProjectById(@PathVariable Long id) {
        WaterProjectDTO project = waterProjectService.getProjectById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<WaterProjectDTO>> getProjectsByTag(@RequestParam String tag) {
        return ResponseEntity.ok(waterProjectService.getProjectsByTag(tag));
    }
}
