package com.baluga.module.waterlevel.service;

import com.baluga.module.waterlevel.entity.Weather;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

public interface IWeatherService extends IService<Weather> {
    List<Weather> getWeatherByLocation(String location);

    List<Weather> getWeatherByLocationAndTime(String location, Date startTime, Date endTime);

    Weather getLatestWeather(String location);

    // 鏂板鏂规硶
    List<Weather> getAllLatestWeathers();
}

