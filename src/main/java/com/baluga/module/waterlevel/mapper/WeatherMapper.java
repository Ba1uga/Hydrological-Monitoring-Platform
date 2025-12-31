package com.baluga.module.waterlevel.mapper;

import com.baluga.module.waterlevel.entity.Weather;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface WeatherMapper extends BaseMapper<Weather> {
    /**
     * 鏍规嵁鍦板尯鏌ヨ澶╂皵淇℃伅
     */
    @Select("SELECT * FROM weather WHERE location = #{location} ORDER BY forecast_time DESC")
    List<Weather> selectWeatherByLocation(@Param("location") String location);

    /**
     * 鏍规嵁鍦板尯鍜屾椂闂磋寖鍥存煡璇㈠ぉ姘?
     */
    @Select("SELECT * FROM weather WHERE location = #{location} AND forecast_time BETWEEN #{startTime} AND #{endTime} ORDER BY forecast_time ASC")
    List<Weather> selectWeatherByLocationAndTime(
            @Param("location") String location,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    /**
     * 鑾峰彇鏈€鏂板ぉ姘旈鎶?
     */
    @Select("SELECT * FROM weather WHERE location = #{location} ORDER BY forecast_time DESC LIMIT 1")
    Weather getLatestWeather(@Param("location") String location);
}

