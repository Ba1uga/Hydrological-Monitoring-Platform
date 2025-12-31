-- 水利工程表
CREATE TABLE IF NOT EXISTS water_project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '工程名称',
    type VARCHAR(50) COMMENT '工程类型',
    tags TEXT COMMENT '标签(JSON格式)',
    location VARCHAR(200) COMMENT '位置',
    river VARCHAR(100) COMMENT '所在河流',
    built_year VARCHAR(50) COMMENT '建设年限',
    capacity VARCHAR(50) COMMENT '库容',
    power VARCHAR(50) COMMENT '装机容量',
    height VARCHAR(50) COMMENT '坝高',
    length VARCHAR(50) COMMENT '坝长',
    investment VARCHAR(50) COMMENT '投资金额',
    annual_power VARCHAR(50) COMMENT '年发电量',
    description TEXT COMMENT '工程描述',
    importance TEXT COMMENT '工程重要性',
    color VARCHAR(20) COMMENT '显示颜色',
    icon VARCHAR(10) COMMENT '图标字符',
    coordinate_lng DOUBLE COMMENT '经度',
    coordinate_lat DOUBLE COMMENT '纬度',
    img_url VARCHAR(500) COMMENT '小图URL',
    large_img_url VARCHAR(500) COMMENT '大图URL',
    chart_capacity DOUBLE COMMENT '图表-库容',
    chart_power DOUBLE COMMENT '图表-装机容量',
    chart_height DOUBLE COMMENT '图表-坝高',
    chart_length DOUBLE COMMENT '图表-长度',
    chart_year_range INT COMMENT '图表-建设周期',
    chart_importance INT COMMENT '图表-重要性评分'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='水利工程表';

-- 主要河流表
CREATE TABLE IF NOT EXISTS major_river (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '河流名称',
    points TEXT COMMENT '坐标点(JSON格式)',
    color VARCHAR(20) COMMENT '显示颜色',
    width INT COMMENT '线宽'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='主要河流表';
