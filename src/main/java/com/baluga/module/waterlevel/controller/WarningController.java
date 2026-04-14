package com.baluga.module.waterlevel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baluga.module.waterlevel.dto.WarningDTO;
import com.baluga.module.waterlevel.entity.Warning;
import com.baluga.module.waterlevel.service.IWarningService;
import com.baluga.module.waterlevel.util.Result;

import jakarta.annotation.Resource;

@RestController("WaterLevelWarningController")
@RequestMapping("/api/waterlevel/warning")
public class WarningController {

    @Resource(name = "WaterLevelWarningService")
    private IWarningService warningService;

    @GetMapping("/list")
    public Result<List<Warning>> getAllWarnings() {
        try {
            return Result.success(warningService.list());
        } catch (Exception ex) {
            return Result.error("预警数据加载失败");
        }
    }

    @GetMapping("/station/{stationId}")
    public Result<List<Warning>> getWarningByStationId(@PathVariable Long stationId) {
        try {
            return Result.success(warningService.getWarningByStationId(stationId));
        } catch (Exception ex) {
            return Result.error("站点预警数据加载失败");
        }
    }

    @GetMapping("/unprocessed")
    public Result<List<Warning>> getUnprocessedWarnings() {
        try {
            return Result.success(warningService.getUnprocessedWarnings());
        } catch (Exception ex) {
            return Result.error("未处理预警数据加载失败");
        }
    }

    @GetMapping("/level/{level}")
    public Result<List<Warning>> getWarningByLevel(@PathVariable Integer level) {
        try {
            return Result.success(warningService.getWarningByLevel(level));
        } catch (Exception ex) {
            return Result.error("按等级加载预警失败");
        }
    }

    @PutMapping("/process/{id}")
    public Result<Boolean> markWarningAsProcessed(@PathVariable Long id) {
        try {
            return Result.success(warningService.markWarningAsProcessed(id));
        } catch (Exception ex) {
            return Result.error("预警处理失败");
        }
    }

    @PostMapping("/add")
    public Result<Boolean> addWarning(@RequestBody WarningDTO warningDTO) {
        try {
            Warning warning = new Warning();
            warning.setStationId(warningDTO.getStationId());
            warning.setTitle(warningDTO.getTitle());
            warning.setContent(warningDTO.getContent());
            warning.setLevel(warningDTO.getLevel());
            warning.setStatus(0);
            return Result.success(warningService.save(warning));
        } catch (Exception ex) {
            return Result.error("新增预警失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteWarning(@PathVariable Long id) {
        try {
            return Result.success(warningService.removeById(id));
        } catch (Exception ex) {
            return Result.error("删除预警失败");
        }
    }
}
