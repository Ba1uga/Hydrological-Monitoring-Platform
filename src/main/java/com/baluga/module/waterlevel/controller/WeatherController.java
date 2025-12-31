package com.baluga.module.waterlevel.controller;

import com.baluga.module.waterlevel.entity.Weather;
import com.baluga.module.waterlevel.service.IWeatherService;
import com.baluga.module.waterlevel.util.Result;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController("WaterLevelWeatherController")
@RequestMapping("/api/waterlevel/weather")
public class WeatherController {
    @Resource
    private IWeatherService weatherService;

    // 鏂板锛氳幏鍙栨墍鏈夊湴鍖烘渶鏂板ぉ姘?
    @GetMapping("/latest/all")
    public Result<List<Weather>> getAllLatestWeathers() {
        return Result.success(weatherService.getAllLatestWeathers());
    }

    // 鍘熸湁鎺ュ彛淇濇寔涓嶅彉...
    @GetMapping("/list")
    public Result<List<Weather>> getAllWeathers() {
        return Result.success(weatherService.list());
    }

    @GetMapping("/location/{location}")
    public Result<List<Weather>> getWeatherByLocation(@PathVariable String location) {
        return Result.success(weatherService.getWeatherByLocation(location));
    }

    @GetMapping("/location/time")
    public Result<List<Weather>> getWeatherByLocationAndTime(
            @RequestParam String location,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        return Result.success(weatherService.getWeatherByLocationAndTime(location, startTime, endTime));
    }

    @GetMapping("/latest/{location}")
    public Result<Weather> getLatestWeather(@PathVariable String location) {
        return Result.success(weatherService.getLatestWeather(location));
    }

    @PostMapping("/add")
    public Result<Boolean> addWeather(@RequestBody Weather weather) {
        return Result.success(weatherService.save(weather));
    }

    @PutMapping("/update")
    public Result<Boolean> updateWeather(@RequestBody Weather weather) {
        return Result.success(weatherService.updateById(weather));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteWeather(@PathVariable Long id) {
        return Result.success(weatherService.removeById(id));
    }
}

