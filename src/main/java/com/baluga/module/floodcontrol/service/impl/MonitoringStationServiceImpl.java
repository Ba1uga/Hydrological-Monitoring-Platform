package com.baluga.module.floodcontrol.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baluga.module.floodcontrol.mapper.MonitoringStationHistoryMapper;
import com.baluga.module.floodcontrol.mapper.MonitoringStationMapper;
import com.baluga.module.floodcontrol.pojo.MonitoringStation;
import com.baluga.module.floodcontrol.pojo.MonitoringStationHistory;
import com.baluga.module.floodcontrol.service.MonitoringStationService;
import com.baluga.module.floodcontrol.vo.DashboardCardVO;
import com.baluga.module.floodcontrol.vo.MonitoringStationHistoryVO;
import com.baluga.module.floodcontrol.vo.MonitoringStationVO;
import com.baluga.module.floodcontrol.vo.TrendAnalysisVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 监测站点服务实现类
 */
@Service
public class MonitoringStationServiceImpl extends ServiceImpl<MonitoringStationMapper, MonitoringStation> implements MonitoringStationService {

    @Autowired
    private MonitoringStationHistoryMapper historyMapper;

    @Value("${app.demoTime.fixed:}")
    private String demoTimeFixed;

    @Value("${app.demoTime.offsetSeconds:0}")
    private long demoTimeOffsetSeconds;

    public LocalDateTime now() {
        LocalDateTime baseTime = parseFixedDemoTime(demoTimeFixed);
        if (baseTime == null) {
            baseTime = LocalDateTime.now();
        }
        if (demoTimeOffsetSeconds != 0) {
            baseTime = baseTime.plusSeconds(demoTimeOffsetSeconds);
        }
        return baseTime;
    }

    public LocalDate currentCacheDate() {
        return now().toLocalDate();
    }

    public LocalDateTime currentCacheHour() {
        return now().truncatedTo(ChronoUnit.HOURS);
    }

    private static LocalDateTime parseFixedDemoTime(String fixed) {
        if (fixed == null) return null;
        String trimmed = fixed.trim();
        if (trimmed.isEmpty()) return null;
        try {
            return LocalDateTime.parse(trimmed);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException ignored) {
        }
        return null;
    }

    @Override
    public List<MonitoringStationHistoryVO> getHistoryList(LocalDateTime startDate, LocalDateTime endDate, String stationName) {
        return historyMapper.searchHistory(startDate, endDate, stationName);
    }

    @Override
    @Cacheable(value = "warningStations", key = "#mode + ':' + #root.target.currentCacheDate()", unless = "#result == null")
    public List<MonitoringStation> getWarningStations(String mode) {
        LocalDate today = now().toLocalDate();
        QueryWrapper<MonitoringStation> query = new QueryWrapper<>();
        query.apply("DATE(value_record_time) = {0}", today);
        
        List<MonitoringStation> allToday = this.list(query);
        
        return allToday.stream()
                .filter(this::checkIsWarning)
                .filter(s -> checkMode(s, mode))
                .collect(Collectors.toList());
    }

    /**
     * 辅助方法：检查站点是否符合当前模式
     */
    private boolean checkMode(MonitoringStation s, String mode) {
        if ("all".equals(mode)) return true;
        if ("flood".equals(mode)) return "m".equals(s.getValueUnit());
        if ("drought".equals(mode)) return "%".equals(s.getValueUnit());
        return true;
    }

    /**
     * 判断站点是否警戒的公共逻辑
     */
    @Override
    public boolean checkIsWarning(MonitoringStation s) {
        if (s == null || s.getAlertThreshold() == null || s.getCurrentValue() == null) return false;
        
        if ("%".equals(s.getValueUnit())) {
            // 抗旱监测逻辑：值 < 阈值 为警戒
            return s.getCurrentValue().compareTo(s.getAlertThreshold()) < 0;
        } else {
            // 防汛监测逻辑：值 > 阈值 为警戒
            return s.getCurrentValue().compareTo(s.getAlertThreshold()) > 0;
        }
    }

    @Override
    public TrendAnalysisVO getTrendAnalysis(String mode) {
        // 1. 获取今日日期 (LocalDateTime 转 LocalDate)
        LocalDate today = now().toLocalDate(); 
        LocalDate yesterday = today.minusDays(1);
        
        // 构建查询条件
        QueryWrapper<MonitoringStationHistory> todayQuery = new QueryWrapper<>();
        todayQuery.apply("DATE(record_date) = {0}", today)
                  .eq("is_warning", 1);
        
        QueryWrapper<MonitoringStationHistory> yesterdayQuery = new QueryWrapper<>();
        yesterdayQuery.apply("DATE(record_date) = {0}", yesterday)
                      .eq("is_warning", 1);
        
        // 根据模式添加过滤条件
        if ("flood".equals(mode)) {
            todayQuery.eq("value_unit", "m");
            yesterdayQuery.eq("value_unit", "m");
        } else if ("drought".equals(mode)) {
            todayQuery.eq("value_unit", "%");
            yesterdayQuery.eq("value_unit", "%");
        }
        
        // 2. 获取今日警戒数
        int currentCount = historyMapper.selectCount(todayQuery).intValue();
        
        // 3. 获取昨日警戒数
        Long yesterdayCountLong = historyMapper.selectCount(yesterdayQuery);
        int yesterdayCount = yesterdayCountLong.intValue();
        
        TrendAnalysisVO vo = new TrendAnalysisVO();
        vo.setCurrentCount(currentCount);
        
        // 额外统计
        if ("all".equals(mode)) {
            QueryWrapper<MonitoringStationHistory> floodQuery = new QueryWrapper<>();
            floodQuery.apply("DATE(record_date) = {0}", today)
                      .eq("value_unit", "m")
                      .eq("is_warning", 1);
            vo.setFloodWarningCount(historyMapper.selectCount(floodQuery).intValue());
            
            QueryWrapper<MonitoringStationHistory> droughtQuery = new QueryWrapper<>();
            droughtQuery.apply("DATE(record_date) = {0}", today)
                        .eq("value_unit", "%")
                        .eq("is_warning", 1);
            vo.setDroughtWarningCount(historyMapper.selectCount(droughtQuery).intValue());
        } else {
            if ("flood".equals(mode)) vo.setFloodWarningCount(currentCount);
            if ("drought".equals(mode)) vo.setDroughtWarningCount(currentCount);
        }

        // 4. 检查是否有昨日数据（用于计算趋势）
        QueryWrapper<MonitoringStationHistory> yesterdayDataCheck = new QueryWrapper<>();
        yesterdayDataCheck.apply("DATE(record_date) = {0}", yesterday);
        
        if ("flood".equals(mode)) yesterdayDataCheck.eq("value_unit", "m");
        if ("drought".equals(mode)) yesterdayDataCheck.eq("value_unit", "%");
        
        boolean hasYesterdayData = historyMapper.selectCount(yesterdayDataCheck) > 0;

        if (hasYesterdayData) {
            // 有昨日数据，正常计算趋势
            vo.setYesterdayCount(yesterdayCount);
            
            double trendPercent = 0.0;
            if (yesterdayCount > 0) {
                trendPercent = ((double)(currentCount - yesterdayCount) / yesterdayCount) * 100;
            } else if (currentCount > 0) {
                trendPercent = 100.0;
            }
            
            vo.setTrendPercentage(String.format("%.1f", Math.abs(trendPercent)));
            
            if (trendPercent > 0) {
                vo.setTrendDirection("up");
            } else if (trendPercent < 0) {
                vo.setTrendDirection("down");
            } else {
                vo.setTrendDirection("stable");
            }
        } else {
            // 无昨日数据，返回 0 而不是提示文本
            vo.setYesterdayCount(0);
            vo.setTrendPercentage("0.0");
            vo.setTrendDirection("stable");
        }
        
        return vo;
    }

    @Override
    public BigDecimal getTotalAffectedArea(String mode) {
        List<MonitoringStation> warnings = getWarningStations(mode);
        return warnings.stream()
                .map(MonitoringStation::getAffectedArea)
                .filter(area -> area != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<MonitoringStationVO> getAllStationVOs(String mode) {
        LocalDate today = now().toLocalDate();
        LocalDate yesterday = today.minusDays(1);
        
        // 1. 查询今日的 monitoring_station 列表
        QueryWrapper<MonitoringStation> query = new QueryWrapper<>();
        query.apply("DATE(value_record_time) = {0}", today);
        if ("flood".equals(mode)) {
            query.eq("value_unit", "m");
        } else if ("drought".equals(mode)) {
            query.eq("value_unit", "%");
        }
        
        List<MonitoringStation> todayList = this.list(query);
        
        // 2. 批量查询这些站点昨日的历史数据
        if (todayList.isEmpty()) {
            return List.of();
        }
        
        // 收集今日站点名称
        List<String> stationNames = todayList.stream().map(MonitoringStation::getStationName).collect(Collectors.toList());
        
        QueryWrapper<MonitoringStation> yesterdayStationQuery = new QueryWrapper<>();
        yesterdayStationQuery.apply("DATE(value_record_time) = {0}", yesterday)
                             .in("station_name", stationNames);
        
        if ("flood".equals(mode)) {
            yesterdayStationQuery.eq("value_unit", "m");
        } else if ("drought".equals(mode)) {
            yesterdayStationQuery.eq("value_unit", "%");
        }
        
        List<MonitoringStation> yesterdayList = this.list(yesterdayStationQuery);
        // Map<StationName_Unit, YesterdayStation>
        java.util.Map<String, MonitoringStation> yesterdayMap = yesterdayList.stream()
                .collect(Collectors.toMap(
                    s -> s.getStationName() + "_" + s.getValueUnit(), 
                    s -> s, 
                    (v1, v2) -> v1
                ));

        return todayList.stream().map(station -> {
            MonitoringStationVO vo = new MonitoringStationVO();
            BeanUtils.copyProperties(station, vo);
            
            // 计算趋势
            String key = station.getStationName() + "_" + station.getValueUnit();
            MonitoringStation yesterdayStation = yesterdayMap.get(key);
            
            if (yesterdayStation != null && yesterdayStation.getCurrentValue() != null && station.getCurrentValue() != null) {
                int compare = station.getCurrentValue().compareTo(yesterdayStation.getCurrentValue());
                if (compare > 0) {
                    vo.setTrendDirection("up");
                } else if (compare < 0) {
                    vo.setTrendDirection("down");
                } else {
                    vo.setTrendDirection("stable");
                }
            } else {
                vo.setTrendDirection("stable"); // 无对比数据默认为平稳
            }
            
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "currentHourStations", key = "#mode + ':' + #root.target.currentCacheHour()", unless = "#result == null")
    public List<MonitoringStationVO> getCurrentHourStationVOs(String mode) {
        // 获取当前时间，并将分钟和秒设置为0，得到当前整点时间
        LocalDateTime base = now();
        LocalDateTime currentHour = base.with(LocalTime.of(base.getHour(), 0, 0));
        
        // 1. 首先尝试查询当前整点的站点数据
        QueryWrapper<MonitoringStation> query = new QueryWrapper<>();
        query.eq("value_record_time", currentHour);
        
        if ("flood".equals(mode)) {
            query.eq("value_unit", "m");
        } else if ("drought".equals(mode)) {
            query.eq("value_unit", "%");
        }
        
        List<MonitoringStation> stations = this.list(query);
        
        // 2. 如果没有当前整点的数据，回退到查询今天的数据
        if (stations.isEmpty()) {
            LocalDate today = now().toLocalDate();
            QueryWrapper<MonitoringStation> todayQuery = new QueryWrapper<>();
            todayQuery.apply("DATE(value_record_time) = {0}", today);
            
            if ("flood".equals(mode)) {
                todayQuery.eq("value_unit", "m");
            } else if ("drought".equals(mode)) {
                todayQuery.eq("value_unit", "%");
            }
            
            stations = this.list(todayQuery);
            
            // 如果今天也没有数据，返回空列表
            if (stations.isEmpty()) {
                return List.of();
            }
        }
        
        // 查询上一个整点的历史数据，用于计算趋势
        LocalDateTime prevHour = currentHour.minusHours(1);
        QueryWrapper<MonitoringStation> prevQuery = new QueryWrapper<>();
        prevQuery.eq("value_record_time", prevHour);
        if ("flood".equals(mode)) {
            prevQuery.eq("value_unit", "m");
        } else if ("drought".equals(mode)) {
            prevQuery.eq("value_unit", "%");
        }
        
        List<MonitoringStation> prevHourStations = this.list(prevQuery);
        
        // Map<StationName_Unit, YesterdayStation>
        java.util.Map<String, MonitoringStation> prevHourMap = prevHourStations.stream()
                .collect(Collectors.toMap(
                    s -> s.getStationName() + "_" + s.getValueUnit(), 
                    s -> s, 
                    (v1, v2) -> v1
                ));
        
        // 转换为VO对象并计算趋势方向
        return stations.stream().map(station -> {
            MonitoringStationVO vo = new MonitoringStationVO();
            BeanUtils.copyProperties(station, vo);
            
            // 计算趋势方向
            String key = station.getStationName() + "_" + station.getValueUnit();
            MonitoringStation prevStation = prevHourMap.get(key);
            
            if (prevStation != null && prevStation.getCurrentValue() != null && station.getCurrentValue() != null) {
                int compare = station.getCurrentValue().compareTo(prevStation.getCurrentValue());
                if (compare > 0) {
                    vo.setTrendDirection("up");
                } else if (compare < 0) {
                    vo.setTrendDirection("down");
                } else {
                    vo.setTrendDirection("stable");
                }
            } else {
                vo.setTrendDirection("stable"); // 无对比数据默认为平稳
            }
            
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MonitoringStationHistoryVO> getHourHistory(LocalDateTime hourTime, String mode) {
        // 计算查询的时间范围，精确到整点
        LocalDateTime startHour = hourTime.with(LocalTime.of(hourTime.getHour(), 0, 0));
        LocalDateTime endHour = startHour.plusHours(1).minusSeconds(1);
        
        // 查询指定整点的历史数据
        List<MonitoringStationHistoryVO> allHistory = historyMapper.searchHistory(startHour, endHour, null);
        
        // 根据模式过滤数据
        return allHistory.stream()
                .filter(history -> checkHistoryMode(history, mode))
                .collect(Collectors.toList());
    }
    
    @Override
    @Cacheable(value = "dashboardCardData", key = "#mode + ':' + #root.target.currentCacheHour()", unless = "#result == null")
    public DashboardCardVO getDashboardCardData(String mode) {
        return getRealTimeCardData(mode);
    }
    
    @Override
    @Cacheable(value = "realTimeCardData", key = "#mode + ':' + #root.target.currentCacheHour()", unless = "#result == null")
    public DashboardCardVO getRealTimeCardData(String mode) {
        // 1. 获取时间切片 (当前整点 & 上一整点)
        LocalDateTime currentHour = now().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime prevHour = currentHour.minusHours(1);
        
        // 2. 根据模式确定值单位
        String valueUnit = "";
        if ("flood".equals(mode)) {
            valueUnit = "m";
        } else if ("drought".equals(mode)) {
            valueUnit = "%";
        }

        // 3. 获取警戒站点数 (查 History 表)
        // 3.1 当前整点警戒数
        Long currentWarningCount = historyMapper.countWarningByTime(currentHour, mode, valueUnit);
        if (currentWarningCount == null) currentWarningCount = 0L;

        // 3.2 上一整点警戒数 (用于计算趋势)
        Long prevWarningCount = historyMapper.countWarningByTime(prevHour, mode, valueUnit);
        if (prevWarningCount == null) prevWarningCount = 0L;

        // 4. 计算趋势 (Trend)
        long diff = currentWarningCount - prevWarningCount;
        String trendDirection;
        
        // 逻辑：警戒数变多是"up"(红色警告)，变少是"down"(绿色缓解)
        if (diff > 0) {
            trendDirection = "up";
        } else if (diff < 0) {
            trendDirection = "down";
        } else {
            trendDirection = "flat";
        }

        // 5. 获取受影响面积
        // 5.1 获取当前整点的警戒站点ID列表
        List<Long> warningStationIds = historyMapper.getWarningStationIdsByTime(currentHour, mode, valueUnit);
        // 5.2 查询这些站点的受影响面积总和
        BigDecimal affectedArea = BigDecimal.ZERO;
        if (!warningStationIds.isEmpty()) {
            affectedArea = baseMapper.sumAffectedAreaByStationIds(warningStationIds);
        }

        // 6. 封装结果返回
        DashboardCardVO vo = new DashboardCardVO();
        vo.setAlertStationCount(currentWarningCount.intValue()); // 警戒数
        vo.setTrend(String.valueOf(Math.abs(diff)));           // 趋势数值 (绝对值)
        vo.setTrendDirection(trendDirection);                  // 趋势方向
        vo.setAffectedArea(affectedArea);                      // 面积
        vo.setDataTime(currentHour);                           // 数据时间
        
        return vo;
    }
    
    @Override
    public List<MonitoringStationHistoryVO> getSevenDaysHistory(String stationName, String mode) {
        // 计算过去7天的时间范围
        LocalDateTime endDate = now();
        LocalDateTime startDate = endDate.minusDays(7);
        
        // 查询指定站点过去7天的历史数据（从 monitoring_station 表查询）
        List<MonitoringStation> list = baseMapper.selectHistoryByStationName(stationName, startDate, endDate, mode);
        
        // 转换为 VO
        return list.stream().map(s -> {
            MonitoringStationHistoryVO vo = new MonitoringStationHistoryVO();
            vo.setId(s.getId());
            vo.setStationName(s.getStationName());
            vo.setStationType(s.getStationType());
            vo.setPrimaryMode(s.getPrimaryMode());
            
            // 核心字段映射
            vo.setRecordDate(s.getValueRecordTime());
            vo.setCurrentValue(s.getCurrentValue());
            vo.setValueUnit(s.getValueUnit());
            
            vo.setFloodRisk(s.getFloodRisk());
            vo.setDroughtRisk(s.getDroughtRisk());
            
            // 判断是否警戒
            vo.setIsWarning(checkIsWarning(s));
            
            return vo;
        }).collect(Collectors.toList());
    }
    
    /**
     * 辅助方法：检查历史数据是否符合当前模式
     */
    private boolean checkHistoryMode(MonitoringStationHistoryVO history, String mode) {
        if ("all".equals(mode)) return true;
        if ("flood".equals(mode)) return "m".equals(history.getValueUnit());
        if ("drought".equals(mode)) return "%".equals(history.getValueUnit());
        return true;
    }
}
