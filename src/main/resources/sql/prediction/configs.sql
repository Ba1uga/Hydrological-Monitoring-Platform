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

 Date: 31/12/2025 10:57:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for configs
-- ----------------------------
DROP TABLE IF EXISTS `configs`;
CREATE TABLE `configs`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `key_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `key_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置值',
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_key_name`(`key_name` ASC) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of configs
-- ----------------------------
INSERT INTO `configs` VALUES (1, 'map.center.lng', '104.0', 'map', '地图中心点经度', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (2, 'map.center.lat', '35.0', 'map', '地图中心点纬度', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (3, 'map.zoom', '5', 'map', '地图默认缩放级别', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (4, 'map.api.key', 'a6d9aeded24e4453798fb33a0a736050', 'map', '高德地图API Key', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (5, 'chart.color.ph', '#37d3a0', 'chart', 'pH值图表颜色', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (6, 'chart.color.do', '#4dabf7', 'chart', '溶解氧图表颜色', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (7, 'chart.color.cod', '#ff9800', 'chart', 'COD图表颜色', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (8, 'chart.color.level', '#f8e71c', 'chart', '水位图表颜色', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (9, 'station.color.i', '#00a854', 'station', 'I类水质站点颜色', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (10, 'station.color.ii', '#a3d930', 'station', 'II类水质站点颜色', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (11, 'station.color.iii', '#f8e71c', 'station', 'III类水质站点颜色', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (12, 'station.color.iv', '#ff9800', 'station', 'IV类水质站点颜色', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (13, 'station.color.v', '#ff5722', 'station', 'V类水质站点颜色', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (14, 'warning.auto.cycle', '4000', 'warning', '预警自动轮播间隔(毫秒)', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (15, 'warning.max.count', '5', 'warning', '最大显示预警数量', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (16, 'api.base.url', 'http://localhost:8080/api', 'api', '后端API基础地址', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (17, 'api.stations.endpoint', '/stations/frontend/all', 'api', '站点数据接口', '2025-12-29 12:48:25', '2025-12-29 12:48:25');
INSERT INTO `configs` VALUES (18, 'api.timeout', '8000', 'api', 'API请求超时时间(毫秒)', '2025-12-29 12:48:25', '2025-12-29 12:48:25');

SET FOREIGN_KEY_CHECKS = 1;
