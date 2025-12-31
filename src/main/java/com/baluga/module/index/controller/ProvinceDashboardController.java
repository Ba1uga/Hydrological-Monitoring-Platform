package com.baluga.module.index.controller;

import com.baluga.module.index.dto.ProvinceDashboardResponse;
import com.baluga.module.index.service.ProvinceDashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/province")
public class ProvinceDashboardController {

    private final ProvinceDashboardService dashboardService;

    public ProvinceDashboardController(ProvinceDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ProvinceDashboardResponse dashboard(@RequestParam("adcode") String adcode,
                                               @RequestParam(value = "name", required = false) String name) {
        // name 目前前端只是展示用；后端按 adcode 查库即可
        return dashboardService.load(adcode);
    }
}
