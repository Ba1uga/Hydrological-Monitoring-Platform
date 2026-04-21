package com.baluga.module.floodcontrol.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.baluga.module.floodcontrol.pojo.MonitoringStation;
import com.baluga.module.floodcontrol.vo.DashboardCardVO;
import com.baluga.module.floodcontrol.vo.MonitoringStationHistoryVO;
import com.baluga.module.floodcontrol.vo.MonitoringStationVO;
import com.baluga.module.floodcontrol.vo.TrendAnalysisVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 监测站点服务接口
 */
public interface MonitoringStationService extends IService<MonitoringStation> {

    /**
     * 获取处于警戒状态的站点列表
     * @param mode 模式：all-全部, flood-防汛, drought-抗旱
     */
    List<MonitoringStation> getWarningStations(String mode);

    /**
     * 判断单条数据是否警戒
     */
    boolean checkIsWarning(MonitoringStation station);

    /**
     * 获取趋势分析数据
     * @param mode 模式：all-全部, flood-防汛, drought-抗旱
     */
    TrendAnalysisVO getTrendAnalysis(String mode);
    
    /**
     * 获取受影响总面积
     * @param mode 模式：all-全部, flood-防汛, drought-抗旱
     */
    BigDecimal getTotalAffectedArea(String mode);
    

    /**
     * 获取所有站点的VO列表（用于展示）
     * @param mode 模式：all-全部, flood-防汛, drought-抗旱
     */
    List<MonitoringStationVO> getAllStationVOs(String mode);

    /**
     * 查询历史数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param stationName 站点名称（模糊查询）
     */
    List<MonitoringStationHistoryVO> getHistoryList(LocalDateTime startDate, LocalDateTime endDate, String stationName);

    Map<String, Object> getHistoryPage(LocalDateTime startDate, LocalDateTime endDate, String stationName, int page, int size);
    
    /**
     * 获取当前整点的站点数据
     * @param mode 模式：all-全部, flood-防汛, drought-抗旱
     */
    default List<MonitoringStationVO> getCurrentHourStationVOs(String mode) {
        return getCurrentHourStationVOs(mode, null);
    }

    List<MonitoringStationVO> getCurrentHourStationVOs(String mode, LocalDateTime queryTime);
    
    /**
     * 获取指定整点的历史数据
     * @param hourTime 指定的整点时间
     * @param mode 模式：all-全部, flood-防汛, drought-抗旱
     */
    List<MonitoringStationHistoryVO> getHourHistory(LocalDateTime hourTime, String mode);
    
    /**
     * 获取仪表盘卡片数据
     * @param mode 模式：all-全部, flood-防汛, drought-抗旱
     * @return 仪表盘卡片数据
     */
    DashboardCardVO getDashboardCardData(String mode);
    
    /**
     * 获取实时卡片数据
     * @param mode 模式：all-全部, flood-防汛, drought-抗旱
     * @return 实时卡片数据
     */
    default DashboardCardVO getRealTimeCardData(String mode) {
        return getRealTimeCardData(mode, null);
    }

    DashboardCardVO getRealTimeCardData(String mode, LocalDateTime queryTime);
    
    /**
     * 获取过去七天的站点数据
     * @param stationName 站点名称
     * @param mode 模式
     * @return 过去七天的站点历史数据
     */
    default List<MonitoringStationHistoryVO> getSevenDaysHistory(String stationName, String mode) {
        return getSevenDaysHistory(stationName, mode, null);
    }

    List<MonitoringStationHistoryVO> getSevenDaysHistory(String stationName, String mode, LocalDateTime queryTime);
}
