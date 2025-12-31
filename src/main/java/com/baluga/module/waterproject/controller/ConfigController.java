package com.baluga.module.waterproject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${amap.key:}")
    private String amapKey;

    @Value("${amap.security-js-code:}")
    private String amapSecurityJsCode;

    @GetMapping("/amap")
    public ResponseEntity<Map<String, String>> getAmapConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("key", amapKey);
        config.put("securityJsCode", amapSecurityJsCode);
        return ResponseEntity.ok(config);
    }
}
