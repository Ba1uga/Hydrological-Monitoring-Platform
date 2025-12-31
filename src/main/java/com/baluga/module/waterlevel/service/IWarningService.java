package com.baluga.module.waterlevel.service;

import java.util.List;

import com.baluga.module.waterlevel.entity.Warning;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IWarningService extends IService<Warning> {
    /**
     * 鏍规嵁绔欑偣ID鏌ヨ棰勮淇℃伅
     */
    List<Warning> getWarningByStationId(Long stationId);

    /**
     * 鏌ヨ鏈鐞嗛璀?
     */
    List<Warning> getUnprocessedWarnings();

    /**
     * 鏍规嵁棰勮绛夌骇鏌ヨ
     */
    List<Warning> getWarningByLevel(Integer level);

    /**
     * 鏍囪棰勮涓哄凡澶勭悊
     */
    boolean markWarningAsProcessed(Long id);
}

