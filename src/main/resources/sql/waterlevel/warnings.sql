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

 Date: 31/12/2025 10:58:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for warnings
-- ----------------------------
DROP TABLE IF EXISTS `warnings`;
CREATE TABLE `warnings`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预警ID',
  `station_id` int NOT NULL COMMENT '站点ID',
  `warning_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `warning_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `parameter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `value` decimal(38, 2) NOT NULL,
  `threshold` decimal(38, 2) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `warning_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_station_id`(`station_id` ASC) USING BTREE,
  INDEX `idx_warning_level`(`warning_level` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  INDEX `idx_warnings_query`(`station_id` ASC, `status` ASC, `warning_level` ASC, `created_at` DESC) USING BTREE,
  CONSTRAINT `warnings_ibfk_1` FOREIGN KEY (`station_id`) REFERENCES `stations` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '预警信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of warnings
-- ----------------------------
INSERT INTO `warnings` VALUES (1, 2, 'water_quality', 'warning', 'COD', 18.50, 20.00, '黄河郑州站COD接近超标阈值', '10:25', 'active', '2025-12-29 12:48:25', NULL);
INSERT INTO `warnings` VALUES (2, 4, 'water_quality', 'serious', '氨氮', 1.20, 1.00, '太湖监测站氨氮超标', '14:30', 'active', '2025-12-29 12:48:25', NULL);
INSERT INTO `warnings` VALUES (3, 5, 'water_level', 'caution', '水位', 115.80, 110.00, '松花江哈尔滨站水位偏高', '09:15', 'active', '2025-12-29 12:48:25', NULL);
INSERT INTO `warnings` VALUES (4, 4, 'water_quality', 'warning', 'pH', 8.20, 8.50, '太湖监测站pH值偏高', '11:45', 'resolved', '2025-12-29 12:48:25', NULL);
INSERT INTO `warnings` VALUES (5, 2, 'water_quality', 'caution', '溶解氧', 5.20, 6.00, '黄河郑州站溶解氧偏低', '16:05', 'active', '2025-12-29 12:48:25', NULL);
INSERT INTO `warnings` VALUES (6, 8, 'water_quality', 'warning', 'COD', 16.70, 20.00, '四川成都监测站COD较高', '13:20', 'active', '2025-12-29 12:48:25', NULL);
INSERT INTO `warnings` VALUES (7, 1, 'water_level', 'normal', '水位', 25.30, 26.00, '长江武汉站水位正常但接近警戒线', '08:45', 'resolved', '2025-12-29 12:48:25', NULL);

SET FOREIGN_KEY_CHECKS = 1;
