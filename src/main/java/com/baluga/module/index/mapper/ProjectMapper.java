package com.baluga.module.index.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProjectMapper {

    @Select("""
        SELECT project_name AS name,
               val          AS val
        FROM project_summary
        WHERE adcode = #{adcode}
        ORDER BY sort_no ASC, id ASC
    """)
    @Results(id = "ProjectNameValueRowMap", value = {
            @Result(column = "name", property = "name"),
            @Result(column = "val",  property = "value")
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
