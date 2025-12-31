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

 Date: 31/12/2025 11:23:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for water_level
-- ----------------------------
DROP TABLE IF EXISTS `water_level`;
CREATE TABLE `water_level`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `station_id` bigint NOT NULL COMMENT '站点ID',
  `level` double NOT NULL COMMENT '水位值(米)',
  `flow_rate` double NULL DEFAULT NULL COMMENT '流速(m/s)',
  `temperature` double NULL DEFAULT NULL COMMENT '水温(℃)',
  `record_time` datetime NOT NULL COMMENT '记录时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '正常' COMMENT '状态：正常/预警/离线',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_station_time`(`station_id` ASC, `record_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水位数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of water_level
-- ----------------------------
INSERT INTO `water_level` VALUES (1, 1, 2.34, 1.8, 18.5, '2025-12-29 09:20:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (2, 1, 2.3, 1.7, 18.3, '2025-12-29 09:11:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (3, 1, 2.25, 1.6, 18, '2025-12-29 09:01:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (4, 2, 3.12, 2.3, 20.1, '2025-12-29 09:19:58', '2025-12-29 09:21:58', '预警', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (5, 2, 3.08, 2.2, 19.8, '2025-12-29 09:09:58', '2025-12-29 09:21:58', '预警', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (6, 2, 3.02, 2.1, 19.5, '2025-12-29 08:59:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (7, 3, 1.89, 1.2, 25.3, '2025-12-29 09:18:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (8, 3, 1.85, 1.1, 25, '2025-12-29 09:08:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (9, 3, 1.8, 1, 24.8, '2025-12-29 08:58:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (10, 4, 2.15, 0.9, 26.5, '2025-12-29 07:21:58', '2025-12-29 09:21:58', '离线', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (11, 4, 2.12, 0.8, 26.2, '2025-12-29 06:21:58', '2025-12-29 09:21:58', '离线', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (12, 5, 3.45, 2.8, 22, '2025-12-29 09:16:58', '2025-12-29 09:21:58', '预警', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (13, 5, 3.42, 2.7, 21.8, '2025-12-29 09:06:58', '2025-12-29 09:21:58', '预警', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (14, 6, 1.75, 0.8, 19.5, '2025-12-29 09:17:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (15, 6, 1.72, 0.7, 19.3, '2025-12-29 09:07:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (16, 7, 2.85, 1.9, 21, '2025-12-29 09:15:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (17, 7, 2.82, 1.8, 20.8, '2025-12-29 09:05:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (18, 8, 2.45, 1.3, 19, '2025-12-29 09:14:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');
INSERT INTO `water_level` VALUES (19, 8, 2.42, 1.2, 18.8, '2025-12-29 09:04:58', '2025-12-29 09:21:58', '正常', '2025-12-29 09:21:58');

SET FOREIGN_KEY_CHECKS = 1;
