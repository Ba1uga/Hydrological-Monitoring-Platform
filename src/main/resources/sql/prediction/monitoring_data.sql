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

 Date: 31/12/2025 10:58:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for monitoring_data
-- ----------------------------
DROP TABLE IF EXISTS `monitoring_data`;
CREATE TABLE `monitoring_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `station_id` int NOT NULL COMMENT '站点ID',
  `data_time` datetime NOT NULL COMMENT '数据时间',
  `ph` double NULL DEFAULT NULL,
  `dissolved_oxygen` double NULL DEFAULT NULL,
  `cod` double NULL DEFAULT NULL,
  `ammonia_nitrogen` double NULL DEFAULT NULL,
  `total_phosphorus` double NULL DEFAULT NULL,
  `total_nitrogen` double NULL DEFAULT NULL,
  `water_level` double NULL DEFAULT NULL,
  `flow_rate` double NULL DEFAULT NULL,
  `water_temperature` double NULL DEFAULT NULL,
  `water_quality_category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `data_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `is_predicted` tinyint(1) NULL DEFAULT 0 COMMENT '是否为预测数据',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_station_time`(`station_id` ASC, `data_time` ASC) USING BTREE,
  INDEX `idx_data_time`(`data_time` ASC) USING BTREE,
  INDEX `idx_station_id`(`station_id` ASC) USING BTREE,
  INDEX `idx_water_quality_category`(`water_quality_category` ASC) USING BTREE,
  INDEX `idx_is_predicted`(`is_predicted` ASC) USING BTREE,
  INDEX `idx_monitoring_data_query`(`station_id` ASC, `data_time` DESC, `is_predicted` ASC) USING BTREE,
  INDEX `idx_monitoring_data_date_range`(`data_time` ASC, `station_id` ASC) USING BTREE,
  CONSTRAINT `monitoring_data_ibfk_1` FOREIGN KEY (`station_id`) REFERENCES `stations` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '监测数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of monitoring_data
-- ----------------------------
INSERT INTO `monitoring_data` VALUES (1, 1, '2025-12-20 08:00:00', 7.07, 7.03, 12.72, 0.135, NULL, NULL, 25.39, NULL, 15.31, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (2, 1, '2025-12-21 08:00:00', 7.18, 6.92, 12.25, 0.148, NULL, NULL, 25.26, NULL, 21.51, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (3, 1, '2025-12-22 08:00:00', 7.12, 6.58, 12.55, 0.152, NULL, NULL, 25.15, NULL, 22.46, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (4, 1, '2025-12-23 08:00:00', 7.31, 6.84, 12.19, 0.173, NULL, NULL, 25.38, NULL, 25.76, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (5, 1, '2025-12-24 08:00:00', 7.28, 6.59, 11.97, 0.149, NULL, NULL, 25.15, NULL, 23.67, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (6, 1, '2025-12-25 08:00:00', 7.21, 6.67, 12.7, 0.143, NULL, NULL, 25.22, NULL, 28.37, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (7, 1, '2025-12-26 08:00:00', 7.15, 6.87, 11.98, 0.184, NULL, NULL, 25.37, NULL, 17.19, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (8, 1, '2025-12-27 08:00:00', 7.09, 6.89, 13.2, 0.181, NULL, NULL, 25.2, NULL, 20.48, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (9, 1, '2025-12-28 08:00:00', 7.16, 7.03, 12.71, 0.133, NULL, NULL, 25.37, NULL, 24.98, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (10, 1, '2025-12-29 08:00:00', 7.17, 6.77, 13.23, 0.163, NULL, NULL, 25.2, NULL, 29.67, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (11, 2, '2025-12-20 08:00:00', 7.81, 5.09, 18.96, 0.768, NULL, NULL, 88.49, NULL, 26.76, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (12, 2, '2025-12-21 08:00:00', 7.83, 5.31, 18.94, 0.832, NULL, NULL, 88.57, NULL, 17.1, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (13, 2, '2025-12-22 08:00:00', 7.74, 4.99, 18.55, 0.79, NULL, NULL, 88.47, NULL, 26.28, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (14, 2, '2025-12-23 08:00:00', 7.88, 5.23, 18.49, 0.827, NULL, NULL, 88.46, NULL, 22.09, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (15, 2, '2025-12-24 08:00:00', 7.94, 5.14, 19.22, 0.83, NULL, NULL, 88.36, NULL, 26.63, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (16, 2, '2025-12-25 08:00:00', 7.76, 5.35, 19.07, 0.85, NULL, NULL, 88.46, NULL, 27.12, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (17, 2, '2025-12-26 08:00:00', 7.8, 5.3, 19.22, 0.829, NULL, NULL, 88.35, NULL, 25.2, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (18, 2, '2025-12-27 08:00:00', 7.91, 5.44, 18.28, 0.833, NULL, NULL, 88.38, NULL, 29.36, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (19, 2, '2025-12-28 08:00:00', 7.88, 5.44, 18.67, 0.762, NULL, NULL, 88.58, NULL, 21.36, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (20, 2, '2025-12-29 08:00:00', 7.85, 5.07, 18.02, 0.769, NULL, NULL, 88.48, NULL, 24.26, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (21, 3, '2025-12-20 08:00:00', 7.12, 7.61, 10.43, 0.113, NULL, NULL, 12.27, NULL, 26.03, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (22, 3, '2025-12-21 08:00:00', 7.09, 7.43, 9.62, 0.153, NULL, NULL, 12.16, NULL, 23.85, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (23, 3, '2025-12-22 08:00:00', 7.14, 7.44, 10.53, 0.149, NULL, NULL, 12.45, NULL, 23.26, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (24, 3, '2025-12-23 08:00:00', 6.93, 7.36, 9.41, 0.123, NULL, NULL, 12.44, NULL, 17.51, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (25, 3, '2025-12-24 08:00:00', 6.96, 7.51, 9.85, 0.078, NULL, NULL, 12.39, NULL, 25.99, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (26, 3, '2025-12-25 08:00:00', 6.86, 7.47, 9.16, 0.076, NULL, NULL, 12.17, NULL, 16.61, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (27, 3, '2025-12-26 08:00:00', 7.07, 7.35, 10.27, 0.116, NULL, NULL, 12.41, NULL, 28.88, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (28, 3, '2025-12-27 08:00:00', 7.05, 7.63, 10.32, 0.163, NULL, NULL, 12.19, NULL, 27.32, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (29, 3, '2025-12-28 08:00:00', 7.06, 7.75, 10.44, 0.134, NULL, NULL, 12.28, NULL, 17.78, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (30, 3, '2025-12-29 08:00:00', 7.01, 7.4, 10.44, 0.141, NULL, NULL, 12.39, NULL, 27.17, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (31, 4, '2025-12-20 08:00:00', 8.22, 4.9, 22.5, 1.225, NULL, NULL, 3.19, NULL, 15.12, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (32, 4, '2025-12-21 08:00:00', 8.26, 4.58, 21.62, 1.223, NULL, NULL, 3.09, NULL, 20.76, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (33, 4, '2025-12-22 08:00:00', 8.18, 4.9, 21.67, 1.246, NULL, NULL, 3.09, NULL, 27.38, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (34, 4, '2025-12-23 08:00:00', 8.31, 5, 22.57, 1.188, NULL, NULL, 3.19, NULL, 17.45, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (35, 4, '2025-12-24 08:00:00', 8.29, 4.68, 22.74, 1.232, NULL, NULL, 3.15, NULL, 17.58, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (36, 4, '2025-12-25 08:00:00', 8.31, 4.97, 22.36, 1.234, NULL, NULL, 3.1, NULL, 18.78, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (37, 4, '2025-12-26 08:00:00', 8.06, 4.88, 21.64, 1.152, NULL, NULL, 3.2, NULL, 22.11, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (38, 4, '2025-12-27 08:00:00', 8.08, 4.91, 21.74, 1.164, NULL, NULL, 3.32, NULL, 17.28, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (39, 4, '2025-12-28 08:00:00', 8.29, 5.02, 21.75, 1.201, NULL, NULL, 3.28, NULL, 19.2, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (40, 4, '2025-12-29 08:00:00', 8.19, 4.58, 22.82, 1.22, NULL, NULL, 3.22, NULL, 25.46, 'IV类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (41, 5, '2025-12-20 08:00:00', 7.58, 6.22, 14.11, 0.426, NULL, NULL, 115.7, NULL, 15.76, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (42, 5, '2025-12-21 08:00:00', 7.47, 5.95, 14.87, 0.438, NULL, NULL, 115.72, NULL, 16.52, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (43, 5, '2025-12-22 08:00:00', 7.47, 6.31, 14.17, 0.49, NULL, NULL, 115.73, NULL, 24.58, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (44, 5, '2025-12-23 08:00:00', 7.5, 6.37, 14.53, 0.475, NULL, NULL, 115.89, NULL, 26.44, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (45, 5, '2025-12-24 08:00:00', 7.47, 6.28, 13.71, 0.455, NULL, NULL, 115.78, NULL, 23.54, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (46, 5, '2025-12-25 08:00:00', 7.62, 6.44, 13.77, 0.483, NULL, NULL, 115.87, NULL, 16.71, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (47, 5, '2025-12-26 08:00:00', 7.6, 5.99, 14.83, 0.405, NULL, NULL, 115.85, NULL, 17.67, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (48, 5, '2025-12-27 08:00:00', 7.48, 6.3, 13.9, 0.405, NULL, NULL, 115.82, NULL, 25.71, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (49, 5, '2025-12-28 08:00:00', 7.49, 6.21, 13.83, 0.438, NULL, NULL, 115.75, NULL, 22.17, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (50, 5, '2025-12-29 08:00:00', 7.56, 6.15, 14.93, 0.441, NULL, NULL, 115.74, NULL, 19.12, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (51, 6, '2025-12-20 08:00:00', 7.21, 6.42, 10.47, 0.208, NULL, NULL, 8.45, NULL, 21.87, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (52, 6, '2025-12-21 08:00:00', 7.29, 6.36, 11.55, 0.3, NULL, NULL, 8.58, NULL, 28.66, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (53, 6, '2025-12-22 08:00:00', 7.19, 6.33, 10.98, 0.23, NULL, NULL, 8.49, NULL, 20.47, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (54, 6, '2025-12-23 08:00:00', 7.35, 6.57, 10.85, 0.238, NULL, NULL, 8.39, NULL, 22.59, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (55, 6, '2025-12-24 08:00:00', 7.38, 6.65, 11.41, 0.281, NULL, NULL, 8.39, NULL, 18.1, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (56, 6, '2025-12-25 08:00:00', 7.44, 6.25, 10.73, 0.292, NULL, NULL, 8.36, NULL, 20.58, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (57, 6, '2025-12-26 08:00:00', 7.25, 6.47, 10.64, 0.234, NULL, NULL, 8.45, NULL, 23.74, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (58, 6, '2025-12-27 08:00:00', 7.43, 6.33, 10.45, 0.252, NULL, NULL, 8.53, NULL, 21.4, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (59, 6, '2025-12-28 08:00:00', 7.24, 6.32, 11.54, 0.223, NULL, NULL, 8.64, NULL, 17.54, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (60, 6, '2025-12-29 08:00:00', 7.38, 6.58, 11.92, 0.295, NULL, NULL, 8.59, NULL, 17.1, 'II类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (61, 7, '2025-12-20 08:00:00', 7.04, 8.12, 8.99, 0.039, NULL, NULL, 150.34, NULL, 22.83, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (62, 7, '2025-12-21 08:00:00', 7.14, 8.05, 7.95, 0.033, NULL, NULL, 150.28, NULL, 25.75, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (63, 7, '2025-12-22 08:00:00', 7.24, 8.34, 9.18, 0.074, NULL, NULL, 150.15, NULL, 19.85, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (64, 7, '2025-12-23 08:00:00', 7.18, 7.97, 9.19, 0.096, NULL, NULL, 150.17, NULL, 15.53, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (65, 7, '2025-12-24 08:00:00', 7.05, 8.27, 8.16, 0.074, NULL, NULL, 150.16, NULL, 23.7, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (66, 7, '2025-12-25 08:00:00', 7.04, 8.08, 8.3, 0.034, NULL, NULL, 150.08, NULL, 19.71, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (67, 7, '2025-12-26 08:00:00', 6.96, 7.95, 9.15, 0.094, NULL, NULL, 150.17, NULL, 16.59, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (68, 7, '2025-12-27 08:00:00', 6.96, 8.44, 8.97, 0.04, NULL, NULL, 150.07, NULL, 15.73, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (69, 7, '2025-12-28 08:00:00', 7.12, 8.36, 8.24, 0.049, NULL, NULL, 150.35, NULL, 21.11, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (70, 7, '2025-12-29 08:00:00', 7.03, 7.96, 8.18, 0.066, NULL, NULL, 150.34, NULL, 25.33, 'I类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (71, 8, '2025-12-20 08:00:00', 7.53, 5.99, 16.84, 0.58, NULL, NULL, 10.57, NULL, 26.72, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (72, 8, '2025-12-21 08:00:00', 7.3, 5.57, 16.95, 0.571, NULL, NULL, 10.37, NULL, 24.3, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (73, 8, '2025-12-22 08:00:00', 7.38, 5.62, 16.63, 0.632, NULL, NULL, 10.58, NULL, 19.29, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (74, 8, '2025-12-23 08:00:00', 7.43, 5.83, 17.39, 0.563, NULL, NULL, 10.57, NULL, 19.95, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (75, 8, '2025-12-24 08:00:00', 7.42, 5.93, 16.01, 0.643, NULL, NULL, 10.51, NULL, 27.56, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (76, 8, '2025-12-25 08:00:00', 7.48, 5.94, 16.92, 0.64, NULL, NULL, 10.51, NULL, 15.67, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (77, 8, '2025-12-26 08:00:00', 7.32, 5.69, 16.92, 0.593, NULL, NULL, 10.41, NULL, 24.97, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (78, 8, '2025-12-27 08:00:00', 7.38, 5.66, 17.19, 0.597, NULL, NULL, 10.61, NULL, 29.62, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (79, 8, '2025-12-28 08:00:00', 7.26, 5.64, 17.05, 0.563, NULL, NULL, 10.49, NULL, 29.55, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (80, 8, '2025-12-29 08:00:00', 7.47, 5.63, 16.75, 0.571, NULL, NULL, 10.49, NULL, 25.34, 'III类', 'valid', 0, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (128, 1, '2025-12-30 08:00:00', 7.28, 6.75, 12.03, 0.133, NULL, NULL, 25.35, NULL, 14.22, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (129, 1, '2025-12-31 08:00:00', 7.23, 6.78, 12.24, 0.17, NULL, NULL, 25.36, NULL, 14.38, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (130, 1, '2026-01-01 08:00:00', 7.2, 6.82, 12.38, 0.133, NULL, NULL, 25.35, NULL, 16.4, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (131, 1, '2026-01-02 08:00:00', 7.2, 6.92, 12.07, 0.156, NULL, NULL, 25.4, NULL, 15.97, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (132, 1, '2026-01-03 08:00:00', 7.25, 6.9, 12.03, 0.155, NULL, NULL, 25.41, NULL, 16.58, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (133, 1, '2026-01-04 08:00:00', 7.26, 6.92, 12.02, 0.144, NULL, NULL, 25.41, NULL, 15.59, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (134, 1, '2026-01-05 08:00:00', 7.24, 6.68, 12.48, 0.128, NULL, NULL, 25.4, NULL, 15.35, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (135, 2, '2025-12-30 08:00:00', 7.8, 5.31, 18.87, 0.814, NULL, NULL, 88.46, NULL, 14.24, 'III类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (136, 2, '2025-12-31 08:00:00', 7.72, 5.34, 18.42, 0.787, NULL, NULL, 88.59, NULL, 16.03, 'III类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (137, 2, '2026-01-01 08:00:00', 7.82, 5.28, 18.06, 0.824, NULL, NULL, 88.57, NULL, 16.29, 'III类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (138, 2, '2026-01-02 08:00:00', 7.71, 5.13, 18.09, 0.81, NULL, NULL, 88.47, NULL, 14.8, 'III类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (139, 2, '2026-01-03 08:00:00', 7.85, 5.26, 18.27, 0.786, NULL, NULL, 88.49, NULL, 14.89, 'III类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (140, 2, '2026-01-04 08:00:00', 7.8, 5.27, 18.23, 0.821, NULL, NULL, 88.61, NULL, 16.39, 'III类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (141, 2, '2026-01-05 08:00:00', 7.7, 5.05, 18.02, 0.777, NULL, NULL, 88.46, NULL, 16.37, 'III类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (142, 3, '2025-12-30 08:00:00', 7.08, 7.42, 9.86, 0.116, NULL, NULL, 12.25, NULL, 15.39, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (143, 3, '2025-12-31 08:00:00', 6.93, 7.7, 10.03, 0.081, NULL, NULL, 12.39, NULL, 16.21, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (144, 3, '2026-01-01 08:00:00', 6.99, 7.7, 9.18, 0.084, NULL, NULL, 12.31, NULL, 15.64, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (145, 3, '2026-01-02 08:00:00', 6.83, 7.63, 9.46, 0.056, NULL, NULL, 12.36, NULL, 16.62, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (146, 3, '2026-01-03 08:00:00', 6.83, 7.62, 9.52, 0.039, NULL, NULL, 12.33, NULL, 16.69, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (147, 3, '2026-01-04 08:00:00', 6.86, 7.67, 9.01, 0.001, NULL, NULL, 12.43, NULL, 15.88, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (148, 3, '2026-01-05 08:00:00', 6.83, 7.77, 9.1, -0.008, NULL, NULL, 12.36, NULL, 16.9, 'I类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (149, 4, '2025-12-30 08:00:00', 8.13, 4.75, 21.81, 1.211, NULL, NULL, 3.14, NULL, 14.21, 'IV类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (150, 4, '2025-12-31 08:00:00', 8.3, 4.67, 22.6, 1.237, NULL, NULL, 3.25, NULL, 14.82, 'IV类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (151, 4, '2026-01-01 08:00:00', 8.19, 4.75, 22.62, 1.236, NULL, NULL, 3.26, NULL, 15.72, 'IV类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (152, 4, '2026-01-02 08:00:00', 8.29, 4.62, 22.78, 1.276, NULL, NULL, 3.22, NULL, 15.36, 'IV类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (153, 4, '2026-01-03 08:00:00', 8.22, 4.73, 22.65, 1.319, NULL, NULL, 3.22, NULL, 16.47, 'IV类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (154, 4, '2026-01-04 08:00:00', 8.33, 4.51, 22.99, 1.305, NULL, NULL, 3.17, NULL, 16.14, 'IV类', 'valid', 1, '2025-12-29 12:48:25');
INSERT INTO `monitoring_data` VALUES (155, 4, '2026-01-05 08:00:00', 8.32, 4.48, 22.62, 1.361, NULL, NULL, 3.17, NULL, 15.66, 'IV类', 'valid', 1, '2025-12-29 12:48:25');

SET FOREIGN_KEY_CHECKS = 1;
