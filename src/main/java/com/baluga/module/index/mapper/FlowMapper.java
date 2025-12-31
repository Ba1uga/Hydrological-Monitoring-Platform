package com.baluga.module.index.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface FlowMapper {

    @Select("""
        SELECT year, series_name AS seriesName, val
        FROM flow_series_value
        WHERE adcode = #{adcode}
        ORDER BY year ASC, series_name ASC
    """)
    List<FlowRow> listByAdcode(@Param("adcode") String adcode);

    class FlowRow {
        private Integer year;
        private String seriesName;
        private BigDecimal val;

        public FlowRow() {}

        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }

        public String getSeriesName() { return seriesName; }
        public void setSeriesName(String seriesName) { this.seriesName = seriesName; }

        public BigDecimal getVal() { return val; }
        public void setVal(BigDecimal val) { this.val = val; }
    }
}
