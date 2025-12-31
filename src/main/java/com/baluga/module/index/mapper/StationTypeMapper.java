package com.baluga.module.index.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StationTypeMapper {

    @Select("""
        SELECT type_name AS name,
               cnt       AS cnt
        FROM station_type_count
        WHERE adcode = #{adcode}
        ORDER BY sort_no ASC, id ASC
    """)
    @Results(id = "StationTypeNameValueRowMap", value = {
            @Result(column = "name", property = "name"),
            @Result(column = "cnt",  property = "value")
    })
    List<NameValueRow> listByAdcode(@Param("adcode") String adcode);

    class NameValueRow {
        private String name;
        private Integer value;

        public NameValueRow() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getValue() { return value; }
        public void setValue(Integer value) { this.value = value; }
    }
}
