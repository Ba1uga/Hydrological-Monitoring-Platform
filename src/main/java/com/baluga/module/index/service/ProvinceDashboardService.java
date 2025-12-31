package com.baluga.module.index.service;

import com.baluga.module.index.dto.ProvinceDashboardResponse;
import com.baluga.module.index.dto.ProvinceDashboardResponse.*;
import com.baluga.module.index.mapper.FlowMapper;
import com.baluga.module.index.mapper.ProjectMapper;
import com.baluga.module.index.mapper.StationTypeMapper;
import com.baluga.module.index.mapper.WaterQualityMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProvinceDashboardService {

    private final StationTypeMapper stationTypeMapper;
    private final FlowMapper flowMapper;
    private final WaterQualityMapper waterQualityMapper;
    private final ProjectMapper projectMapper;

    public ProvinceDashboardService(StationTypeMapper stationTypeMapper,
                                    FlowMapper flowMapper,
                                    WaterQualityMapper waterQualityMapper,
                                    ProjectMapper projectMapper) {
        this.stationTypeMapper = stationTypeMapper;
        this.flowMapper = flowMapper;
        this.waterQualityMapper = waterQualityMapper;
        this.projectMapper = projectMapper;
    }

    public ProvinceDashboardResponse load(String adcode) {
        ProvinceDashboardResponse resp = new ProvinceDashboardResponse();

        // 1) stationType
        List<NameValue> stationType = new ArrayList<>();
        for (StationTypeMapper.NameValueRow r : stationTypeMapper.listByAdcode(adcode)) {
            stationType.add(new NameValue(r.getName(), r.getValue()));
        }
        resp.setStationType(stationType);

        // 2) flow
        var flowRows = flowMapper.listByAdcode(adcode);

        TreeSet<Integer> yearSet = new TreeSet<>();
        for (var r : flowRows) yearSet.add(r.getYear());

        List<String> years = new ArrayList<>();
        for (Integer y : yearSet) years.add(String.valueOf(y));

        LinkedHashMap<String, Map<Integer, Number>> seriesMap = new LinkedHashMap<>();
        for (var r : flowRows) {
            seriesMap.computeIfAbsent(r.getSeriesName(), k -> new HashMap<>())
                    .put(r.getYear(), r.getVal());
        }

        List<Series> seriesList = new ArrayList<>();
        for (var entry : seriesMap.entrySet()) {
            Series s = new Series();
            s.setName(entry.getKey());

            List<Number> data = new ArrayList<>();
            for (String yStr : years) {
                int y = Integer.parseInt(yStr);
                data.add(entry.getValue().getOrDefault(y, 0));
            }
            s.setData(data);
            seriesList.add(s);
        }

        Flow flow = new Flow();
        flow.setYears(years);
        flow.setSeries(seriesList);
        resp.setFlow(flow);

        // 3) waterQuality radar
        var indicatorRows = waterQualityMapper.listIndicators(adcode);
        List<RadarIndicator> indicators = new ArrayList<>();
        List<String> indicatorNames = new ArrayList<>();

        for (var i : indicatorRows) {
            RadarIndicator ri = new RadarIndicator();
            ri.setName(i.getName());
            // 你前端写死用 100 也可以，这里保持兼容：有 maxVal 用 maxVal，否则 100
            ri.setMax(i.getMaxVal() != null ? i.getMaxVal() : 100);
            indicators.add(ri);
            indicatorNames.add(i.getName());
        }

        var wqRows = waterQualityMapper.listValues(adcode);

        // years 动态从 DB 取（如 2023/2024/2025）
        TreeSet<Integer> wqYearSet = new TreeSet<>();
        for (var r : wqRows) wqYearSet.add(r.getYear());

        List<String> wqYears = new ArrayList<>();
        for (Integer y : wqYearSet) wqYears.add(String.valueOf(y));

        // year -> (indicatorName -> value)
        Map<Integer, Map<String, Number>> yearMap = new HashMap<>();
        for (var r : wqRows) {
            yearMap.computeIfAbsent(r.getYear(), k -> new HashMap<>())
                    .put(r.getIndicatorName(), r.getVal());
        }

        List<RadarData> radarData = new ArrayList<>();
        for (String yStr : wqYears) {
            int y = Integer.parseInt(yStr);
            RadarData rd = new RadarData();
            rd.setName(yStr);

            Map<String, Number> m = yearMap.getOrDefault(y, Collections.emptyMap());
            List<Number> vals = new ArrayList<>();
            for (String indName : indicatorNames) {
                vals.add(m.getOrDefault(indName, 0));
            }
            rd.setValue(vals);
            radarData.add(rd);
        }

        WaterQuality wq = new WaterQuality();
        wq.setYears(wqYears);
        wq.setIndicators(indicators);
        wq.setData(radarData);
        resp.setWaterQuality(wq);

        // 4) projects
        List<NameValue> projects = new ArrayList<>();
        for (ProjectMapper.NameValueRow r : projectMapper.listByAdcode(adcode)) {
            projects.add(new NameValue(r.getName(), r.getValue()));
        }
        resp.setProjects(projects);

        return resp;
    }
}
