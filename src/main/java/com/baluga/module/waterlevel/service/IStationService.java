package com.baluga.module.waterlevel.service;

import com.baluga.module.waterlevel.entity.Station;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IStationService extends IService<Station> {
    /**
     * 鏍规嵁绔欑偣鍚嶇О妯＄硦鏌ヨ
     */
    List<Station> getStationByName(String name);

    /**
     * 鏌ヨ鎵€鏈夊湪绾跨珯鐐?
     */
    List<Station> getOnlineStations();

    /**
     * 鏍规嵁ID鏌ヨ绔欑偣璇︽儏
     */
    Station getStationDetail(Long id);
}

