package com.baluga.module.prediction.controller;

import com.baluga.common.Result;
import com.baluga.module.prediction.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController("PredictionConfigController")
@RequestMapping("/api/prediction/config")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ConfigController {

    private final ConfigService configService;

    @GetMapping("/all")
    public Result<Map<String, String>> getAllConfigs() {
        try {
            Map<String, String> configs = configService.getAllConfigs();
            return Result.success(configs);
        } catch (Exception e) {
            return Result.error("获取配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public Result<Map<String, String>> getConfigsByCategory(@PathVariable String category) {
        try {
            Map<String, String> configs = configService.getConfigMapByCategory(category);
            return Result.success(configs);
        } catch (Exception e) {
            return Result.error("获取分类配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/{key}")
    public Result<String> getConfigByKey(@PathVariable String key) {
        try {
            var config = configService.getConfigByKey(key);
            if (config == null) {
                return Result.error("配置不存在");
            }
            return Result.success(config.getKeyValue());
        } catch (Exception e) {
            return Result.error("获取配置失败: " + e.getMessage());
        }
    }
}
