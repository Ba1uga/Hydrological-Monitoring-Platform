package com.baluga.module.waterproject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baluga.module.waterproject.dto.MajorRiverDTO;
import com.baluga.module.waterproject.service.MajorRiverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rivers")
@RequiredArgsConstructor
public class MajorRiverController {

    private final MajorRiverService majorRiverService;

    @GetMapping
    public ResponseEntity<List<MajorRiverDTO>> getAllRivers() {
        return ResponseEntity.ok(majorRiverService.getAllRivers());
    }
}
