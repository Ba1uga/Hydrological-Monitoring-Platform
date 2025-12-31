package com.baluga.module.floodcontrol.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baluga.common.Result;
import com.baluga.module.floodcontrol.service.RescueMaterialService;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private RescueMaterialService resourceService;

    /**
     * 获取防汛物资数据
     */
    @GetMapping("/flood")
    public Result<List<Map<String, Object>>> getFloodResources() {
        return Result.success(resourceService.getFloodMaterials());
    }

    /**
     * 获取抗旱物资数据
     */
    @GetMapping("/drought")
    public Result<List<Map<String, Object>>> getDroughtResources() {
        return Result.success(resourceService.getDroughtMaterials());
    }

    /**
     * 获取指定分类的详细物资列表
     */
    @GetMapping("/{mode}/details")
    public Result<Map<String, Object>> getMaterialDetails(
            @PathVariable("mode") String mode,
            @RequestParam("category") String categoryName) {
        return Result.success(resourceService.getMaterialDetails(mode, categoryName));
    }
}
