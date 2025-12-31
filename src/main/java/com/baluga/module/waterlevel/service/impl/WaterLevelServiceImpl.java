package com.baluga.module.waterlevel.service.impl;

import com.baluga.module.waterlevel.entity.WaterLevel;
import com.baluga.module.waterlevel.mapper.WaterLevelMapper;
import com.baluga.module.waterlevel.service.IWaterLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baluga.module.waterlevel.dto.PredictionParamDTO;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("WaterLevelService")
public class WaterLevelServiceImpl extends ServiceImpl<WaterLevelMapper, WaterLevel> implements IWaterLevelService {
    @Resource
    private WaterLevelMapper waterLevelMapper;

    @Override
    public WaterLevel getLatestLevel(Long stationId) {
        return waterLevelMapper.getLatestLevel(stationId);
    }

    @Override
    public List<WaterLevel> getHistoryLevel(Long stationId, Date startTime, Date endTime) {
        return waterLevelMapper.getLevelByStationAndTime(stationId, startTime, endTime);
    }

    @Override
    public List<WaterLevel> getAllCurrentLevels() {
        return waterLevelMapper.getAllLatestLevels();
    }

    @Override
    public List<WaterLevel> predictLevel(PredictionParamDTO param) {
        // 妯℃嫙姘翠綅棰勬祴閫昏緫锛屽疄闄呴」鐩腑鍙浛鎹负鐪熷疄绠楁硶锛圠STM/ARIMA/SVM锛?
        List<WaterLevel> predictionList = new ArrayList<>();
        WaterLevel latestLevel = getLatestLevel(param.getStationId());
        if (latestLevel == null) {
            return predictionList;
        }

        long interval = param.getSampleRate() * 60 * 1000L;
        double baseLevel = latestLevel.getLevel();
        Date baseTime = latestLevel.getRecordTime();

        for (int i = 1; i <= param.getHours() * 60 / param.getSampleRate(); i++) {
            WaterLevel prediction = new WaterLevel();
            prediction.setStationId(param.getStationId());
            // 妯℃嫙姘翠綅鍙樺寲锛堥殢鏈烘尝鍔紝瀹為檯闇€绠楁硶鏀拺锛?
            double randomChange = (Math.random() - 0.4) * 0.1;
            prediction.setLevel(Math.round((baseLevel + randomChange * i) * 100.0) / 100.0);
            prediction.setFlowRate(latestLevel.getFlowRate() + (Math.random() - 0.5) * 0.2);
            prediction.setTemperature(latestLevel.getTemperature() + (Math.random() - 0.5) * 0.5);
            prediction.setRecordTime(new Date(baseTime.getTime() + interval * i));
            predictionList.add(prediction);
        }
        return predictionList;
    }
}
