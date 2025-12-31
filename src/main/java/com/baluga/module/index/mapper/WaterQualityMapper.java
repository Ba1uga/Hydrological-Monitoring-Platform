package com.baluga.module.index.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface WaterQualityMapper {

    /**
     * 你的表是一年一行，所以这里要按 name 去重（否则会出现 溶解氧/PH/COD 每年重复一遍）
     */
    @Select("""
        SELECT name,
               MAX(value)  AS maxVal,
               MIN(sort_no) AS sortNo
        FROM province_water_quality_indicator
        WHERE adcode = #{adcode}
        GROUP BY name
        ORDER BY sortNo ASC
    """)
    List<IndicatorRow> listIndicators(@Param("adcode") String adcode);

    /**
     * year 在表里是 varchar，这里显式 CAST 成整数，方便 service 里按年组装
     */
    @Select("""
        SELECT CAST(year AS UNSIGNED) AS year,
               name                  AS indicatorName,
               value                 AS val
        FROM province_water_quality_indicator
        WHERE adcode = #{adcode}
        ORDER BY CAST(year AS UNSIGNED) ASC, sort_no ASC, id ASC
    """)
    List<WqValueRow> listValues(@Param("adcode") String adcode);

    class IndicatorRow {
        private String name;
        private BigDecimal maxVal;
        private Integer sortNo;

        public IndicatorRow() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public BigDecimal getMaxVal() { return maxVal; }
        public void setMaxVal(BigDecimal maxVal) { this.maxVal = maxVal; }

        public Integer getSortNo() { return sortNo; }
        public void setSortNo(Integer sortNo) { this.sortNo = sortNo; }
    }

    class WqValueRow {
        private Integer year;
        private String indicatorName;
        private BigDecimal val;

        public WqValueRow() {}

        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }

        public String getIndicatorName() { return indicatorName; }
        public void setIndicatorName(String indicatorName) { this.indicatorName = indicatorName; }

        public BigDecimal getVal() { return val; }
        public void setVal(BigDecimal val) { this.val = val; }
    }
}
