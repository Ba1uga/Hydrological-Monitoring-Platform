package com.baluga.module.index.dto;

import java.util.List;

/**
 * Response DTO for: GET /api/province/dashboard
 * Matches the front-end JSON contract.
 */
public class ProvinceDashboardResponse {

    private List<NameValue> stationType;
    private Flow flow;
    private WaterQuality waterQuality;
    private List<NameValue> projects;

    public List<NameValue> getStationType() { return stationType; }
    public void setStationType(List<NameValue> stationType) { this.stationType = stationType; }

    public Flow getFlow() { return flow; }
    public void setFlow(Flow flow) { this.flow = flow; }

    public WaterQuality getWaterQuality() { return waterQuality; }
    public void setWaterQuality(WaterQuality waterQuality) { this.waterQuality = waterQuality; }

    public List<NameValue> getProjects() { return projects; }
    public void setProjects(List<NameValue> projects) { this.projects = projects; }

    // -------- nested DTOs --------

    public static class NameValue {
        private String name;
        private Number value;

        public NameValue() {}
        public NameValue(String name, Number value) { this.name = name; this.value = value; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Number getValue() { return value; }
        public void setValue(Number value) { this.value = value; }
    }

    public static class Flow {
        private List<String> years;
        private List<Series> series;

        public List<String> getYears() { return years; }
        public void setYears(List<String> years) { this.years = years; }

        public List<Series> getSeries() { return series; }
        public void setSeries(List<Series> series) { this.series = series; }
    }

    public static class Series {
        private String name;
        private List<Number> data;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<Number> getData() { return data; }
        public void setData(List<Number> data) { this.data = data; }
    }

    public static class WaterQuality {
        private List<String> years;
        private List<RadarIndicator> indicators;
        private List<RadarData> data;

        public List<String> getYears() { return years; }
        public void setYears(List<String> years) { this.years = years; }

        public List<RadarIndicator> getIndicators() { return indicators; }
        public void setIndicators(List<RadarIndicator> indicators) { this.indicators = indicators; }

        public List<RadarData> getData() { return data; }
        public void setData(List<RadarData> data) { this.data = data; }
    }

    public static class RadarIndicator {
        private String name;
        private Number max;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Number getMax() { return max; }
        public void setMax(Number max) { this.max = max; }
    }

    public static class RadarData {
        private String name;        // year string
        private List<Number> value; // aligned by indicators order

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<Number> getValue() { return value; }
        public void setValue(List<Number> value) { this.value = value; }
    }
}
