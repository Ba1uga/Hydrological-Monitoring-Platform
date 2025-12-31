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

 Date: 31/12/2025 11:23:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for weather
-- ----------------------------
DROP TABLE IF EXISTS `weather`;
CREATE TABLE `weather`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地区',
  `temperature` double NULL DEFAULT NULL COMMENT '温度(℃)',
  `humidity` double NULL DEFAULT NULL COMMENT '湿度(%)',
  `precipitation` double NULL DEFAULT NULL COMMENT '降水量(mm)',
  `wind_speed` double NULL DEFAULT NULL COMMENT '风速(m/s)',
  `weather_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '天气类型',
  `forecast_time` datetime NOT NULL COMMENT '预报时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_location_time`(`location` ASC, `forecast_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '天气信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of weather
-- ----------------------------
INSERT INTO `weather` VALUES (1, '北京', 18.5, 65, 5.2, 3.2, '多云', '2025-12-29 09:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (2, '北京', 19, 70, 8.5, 3.8, '小雨', '2025-12-29 12:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (3, '北京', 20.5, 68, 2.1, 4.2, '晴转多云', '2025-12-29 15:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (4, '上海', 20.1, 75, 12.3, 4.5, '中雨', '2025-12-29 09:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (5, '上海', 19.8, 78, 15.6, 5.2, '大雨', '2025-12-29 12:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (6, '上海', 21.2, 72, 8.9, 4.8, '小雨', '2025-12-29 15:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (7, '广州', 25.3, 80, 25.6, 2.8, '暴雨', '2025-12-29 09:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (8, '广州', 26, 82, 28.3, 3.2, '暴雨', '2025-12-29 12:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (9, '广州', 26.5, 78, 18.7, 3, '大雨', '2025-12-29 15:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (10, '深圳', 26.5, 78, 22.4, 3.5, '大雨', '2025-12-29 09:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (11, '武汉', 22, 70, 18.9, 4.2, '大雨', '2025-12-29 09:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (12, '成都', 19.5, 68, 8.7, 2.5, '小雨', '2025-12-29 09:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (13, '杭州', 21, 72, 15.3, 3.8, '中雨', '2025-12-29 09:22:38', '2025-12-29 09:22:38');
INSERT INTO `weather` VALUES (14, '南京', 19, 65, 10.8, 3.2, '小雨', '2025-12-29 09:22:38', '2025-12-29 09:22:38');

SET FOREIGN_KEY_CHECKS = 1;
