package com.baluga.module.waterlevel.service.impl;

import com.baluga.module.waterlevel.entity.Weather;
import com.baluga.module.waterlevel.mapper.WeatherMapper;
import com.baluga.module.waterlevel.service.IWeatherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service("WaterLevelWeatherService")
public class WeatherServiceImpl extends ServiceImpl<WeatherMapper, Weather> implements IWeatherService {

    @Override
    public List<Weather> getAllLatestWeathers() {
        // 鏌ヨ姣忎釜鍦板尯鐨勬渶鏂板ぉ姘旇褰?
        QueryWrapper<Weather> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("max(id) as id", "location", "temperature", "humidity",
                        "precipitation", "wind_speed", "weather_type", "forecast_time")
                .groupBy("location");
        return list(queryWrapper);
    }

    // 鍘熸湁鏂规硶瀹炵幇淇濇寔涓嶅彉...
    @Override
    public List<Weather> getWeatherByLocation(String location) {
        QueryWrapper<Weather> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("location", location);
        return list(queryWrapper);
    }

    @Override
    public List<Weather> getWeatherByLocationAndTime(String location, Date startTime, Date endTime) {
        QueryWrapper<Weather> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("location", location)
                .between("forecast_time", startTime, endTime);
        return list(queryWrapper);
    }

    @Override
    public Weather getLatestWeather(String location) {
        QueryWrapper<Weather> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("location", location)
                .orderByDesc("forecast_time")
                .last("limit 1");
        return getOne(queryWrapper);
    }
}
