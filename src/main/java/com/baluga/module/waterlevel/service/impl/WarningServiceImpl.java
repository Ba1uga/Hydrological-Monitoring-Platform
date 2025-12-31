package com.baluga.module.waterlevel.service.impl;

import com.baluga.module.waterlevel.entity.Warning;
import com.baluga.module.waterlevel.mapper.WarningMapper;
import com.baluga.module.waterlevel.service.IWarningService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;

@Service("WaterLevelWarningService")
public class WarningServiceImpl extends ServiceImpl<WarningMapper, Warning> implements IWarningService {
    @Resource
    private WarningMapper warningMapper;

    @Override
    public List<Warning> getWarningByStationId(Long stationId) {
        return warningMapper.selectWarningByStationId(stationId);
    }

    @Override
    public List<Warning> getUnprocessedWarnings() {
        try {
            return warningMapper.selectUnprocessedWarnings();
        } catch (Exception ex) {
            return List.of();
        }
    }

    @Override
    public List<Warning> getWarningByLevel(Integer level) {
        return warningMapper.selectWarningByLevel(level);
    }

    @Override
    public boolean markWarningAsProcessed(Long id) {
        Warning warning = warningMapper.selectById(id);
        if (warning == null) {
            return false;
        }
        warning.setStatus(1);
        return warningMapper.updateById(warning) > 0;
    }
}
