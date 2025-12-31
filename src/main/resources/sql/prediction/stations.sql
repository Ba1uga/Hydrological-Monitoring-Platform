/*
 Navicat Premium Dump SQL

 Source Server         : Test
 Source Server Type    : MySQL
 Source Server Version : 90300 (9.3.0)
 Source Host           : localhost:3306
 Source Schema         : china_hydrology_monitor

 Target Server Type    : MySQL
 Target Server Version : 90300 (9.3.0)
 File Encoding         : 65001

 Date: 31/12/2025 11:24:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for stations
-- ----------------------------
DROP TABLE IF EXISTS `stations`;
CREATE TABLE `stations`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '站点ID',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站点编码',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站点名称',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站点类型：river-河流，lake-湖泊，reservoir-水库',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地理位置',
  `river` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属河流/水系',
  `longitude` double NULL DEFAULT NULL,
  `latitude` double NULL DEFAULT NULL,
  `water_quality` json NULL COMMENT '当前水质数据',
  `quality_class` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '水质类别：I类，II类，III类，IV类，V类',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'normal' COMMENT '站点状态：normal-正常，warning-预警，critical-严重',
  `trend` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '趋势：improve-改善，stable-稳定，decline-下降',
  `tags` json NULL COMMENT '标签，如[\"长江\",\"国控断面\",\"重点监测\"]',
  `icon` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地图图标文字',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#4dabf7' COMMENT '地图标记颜色',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE,
  INDEX `idx_location`(`longitude` ASC, `latitude` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_quality_class`(`quality_class` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '监测站点表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of stations
-- ----------------------------
INSERT INTO `stations` VALUES (1, 'CJ-WH-001', '长江武汉站', 'river', '湖北省武汉市', '长江', 114.305, 30.593, '{\"DO\": 6.8, \"pH\": 7.2, \"COD\": 12.5, \"NH3N\": 0.15, \"level\": 25.3}', 'II类', 'normal', 'stable', '[\"长江\", \"国控断面\"]', '长', '#a3d930', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `stations` VALUES (2, 'HH-ZZ-001', '黄河郑州站', 'river', '河南省郑州市', '黄河', 113.665, 34.758, '{\"DO\": 5.2, \"pH\": 7.8, \"COD\": 18.5, \"NH3N\": 0.8, \"level\": 88.5}', 'III类', 'warning', 'stable', '[\"黄河\", \"省控断面\"]', '黄', '#f8e71c', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `stations` VALUES (3, 'ZJ-GZ-001', '珠江广州站', 'river', '广东省广州市', '珠江', 113.264, 23.129, '{\"DO\": 7.5, \"pH\": 7.0, \"COD\": 9.8, \"NH3N\": 0.12, \"level\": 12.3}', 'II类', 'normal', 'improve', '[\"珠江\", \"国控断面\"]', '珠', '#a3d930', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `stations` VALUES (4, 'TH-WX-001', '太湖监测站', 'lake', '江苏省无锡市', '太湖', 120.138, 31.231, '{\"DO\": 4.8, \"pH\": 8.2, \"COD\": 22.1, \"NH3N\": 1.2, \"level\": 3.2}', 'IV类', 'critical', 'decline', '[\"太湖\", \"重点湖泊\", \"蓝藻监测\"]', '太', '#ff9800', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `stations` VALUES (5, 'SHJ-HEB-001', '松花江哈尔滨站', 'river', '黑龙江省哈尔滨市', '松花江', 126.642, 45.757, '{\"DO\": 6.2, \"pH\": 7.5, \"COD\": 14.3, \"NH3N\": 0.45, \"level\": 115.8}', 'III类', 'warning', 'stable', '[\"松花江\", \"省控断面\"]', '松', '#f8e71c', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `stations` VALUES (6, 'FJ-XM-001', '福建厦门监测站', 'river', '福建省厦门市', '九龙江', 118.089, 24.479, '{\"DO\": 6.5, \"pH\": 7.3, \"COD\": 11.2, \"NH3N\": 0.25, \"level\": 8.5}', 'II类', 'normal', 'stable', '[\"九龙江\", \"入海口\"]', '厦', '#4dabf7', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `stations` VALUES (7, 'BJ-MY-001', '北京密云水库站', 'reservoir', '北京市密云区', '密云水库', 116.832, 40.497, '{\"DO\": 8.2, \"pH\": 7.1, \"COD\": 8.5, \"NH3N\": 0.08, \"level\": 150.2}', 'I类', 'normal', 'stable', '[\"密云水库\", \"饮用水源\"]', '京', '#00a854', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `stations` VALUES (8, 'SC-CD-001', '四川成都监测站', 'river', '四川省成都市', '锦江', 104.066, 30.657, '{\"DO\": 5.8, \"pH\": 7.4, \"COD\": 16.7, \"NH3N\": 0.6, \"level\": 10.5}', 'III类', 'normal', 'improve', '[\"锦江\", \"市控断面\"]', '成', '#a3d930', '2025-12-29 12:48:25', '2025-12-29 12:48:25');

SET FOREIGN_KEY_CHECKS = 1;
