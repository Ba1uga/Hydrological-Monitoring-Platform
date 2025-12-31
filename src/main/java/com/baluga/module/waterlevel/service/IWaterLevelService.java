package com.baluga.module.waterlevel.service;

import com.baluga.module.waterlevel.dto.PredictionParamDTO;
import com.baluga.module.waterlevel.entity.WaterLevel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

public interface IWaterLevelService extends IService<WaterLevel> {
    /**
     * 鑾峰彇绔欑偣鏈€鏂版按浣?
     */
    WaterLevel getLatestLevel(Long stationId);

    /**
     * 鑾峰彇绔欑偣鍘嗗彶姘翠綅鏁版嵁
     */
    List<WaterLevel> getHistoryLevel(Long stationId, Date startTime, Date endTime);

    /**
     * 鑾峰彇鎵€鏈夌珯鐐瑰綋鍓嶆按浣嶆瑙?
     */
    List<WaterLevel> getAllCurrentLevels();

    /**
     * 棰勬祴姘翠綅
     */
    List<WaterLevel> predictLevel(PredictionParamDTO param);
}

