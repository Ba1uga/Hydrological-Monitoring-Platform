package com.baluga.module.waterlevel.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baluga.module.waterlevel.entity.Station;
import com.baluga.module.waterlevel.mapper.StationMapper;
import com.baluga.module.waterlevel.service.IStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.annotation.Resource;

@Service("WaterLevelStationService")
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements IStationService {
    @Resource
    private StationMapper stationMapper;

    @Override
    public List<Station> getStationByName(String name) {
        return stationMapper.selectStationByName(name);
    }

    @Override
    public List<Station> getOnlineStations() {
        return stationMapper.selectOnlineStations();
    }

    @Override
    public Station getStationDetail(Long id) {
        return stationMapper.selectById(id);
    }
}
