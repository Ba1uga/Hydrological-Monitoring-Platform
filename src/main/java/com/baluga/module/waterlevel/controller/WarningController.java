package com.baluga.module.waterlevel.controller;

import com.baluga.module.waterlevel.dto.WarningDTO;
import com.baluga.module.waterlevel.entity.Warning;
import com.baluga.module.waterlevel.service.IWarningService;
import com.baluga.module.waterlevel.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("WaterLevelWarningController")
@RequestMapping("/api/waterlevel/warning")
public class WarningController {
    @Resource(name = "WaterLevelWarningService")
    private IWarningService warningService;

    /**
     * 鏌ヨ鎵€鏈夐璀?
     */
    @GetMapping("/list")
    public Result<List<Warning>> getAllWarnings() {
        try {
            return Result.success(warningService.list());
        } catch (Exception ex) {
            return Result.success(List.of());
        }
    }

    /**
     * 鏍规嵁绔欑偣ID鏌ヨ棰勮淇℃伅
     */
    @GetMapping("/station/{stationId}")
    public Result<List<Warning>> getWarningByStationId(@PathVariable Long stationId) {
        try {
            return Result.success(warningService.getWarningByStationId(stationId));
        } catch (Exception ex) {
            return Result.success(List.of());
        }
    }

    /**
     * 鏌ヨ鏈鐞嗛璀?
     */
    @GetMapping("/unprocessed")
    public Result<List<Warning>> getUnprocessedWarnings() {
        try {
            return Result.success(warningService.getUnprocessedWarnings());
        } catch (Exception ex) {
            return Result.success(List.of());
        }
    }

    /**
     * 鏍规嵁棰勮绛夌骇鏌ヨ
     */
    @GetMapping("/level/{level}")
    public Result<List<Warning>> getWarningByLevel(@PathVariable Integer level) {
        try {
            return Result.success(warningService.getWarningByLevel(level));
        } catch (Exception ex) {
            return Result.success(List.of());
        }
    }

    /**
     * 鏍囪棰勮涓哄凡澶勭悊
     */
    @PutMapping("/process/{id}")
    public Result<Boolean> markWarningAsProcessed(@PathVariable Long id) {
        try {
            return Result.success(warningService.markWarningAsProcessed(id));
        } catch (Exception ex) {
            return Result.success(false);
        }
    }

    /**
     * 鏂板棰勮
     */
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
            return Result.success(false);
        }
    }

    /**
     * 鍒犻櫎棰勮
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteWarning(@PathVariable Long id) {
        try {
            return Result.success(warningService.removeById(id));
        } catch (Exception ex) {
            return Result.success(false);
        }
    }
}

