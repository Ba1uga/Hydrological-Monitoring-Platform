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

 Date: 31/12/2025 11:25:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for station
-- ----------------------------
DROP TABLE IF EXISTS `station`;
CREATE TABLE `station`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '站点ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站点名称',
  `location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站点位置',
  `longitude` double NOT NULL COMMENT '经度',
  `latitude` double NOT NULL COMMENT '纬度',
  `warning_level` double NOT NULL COMMENT '警戒水位',
  `danger_level` double NOT NULL COMMENT '危险水位',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0-离线，1-正常',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '监测站点信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of station
-- ----------------------------
INSERT INTO `station` VALUES (1, '北京通惠河监测站', '北京市朝阳区', 116.39748, 39.908823, 3.5, 4, 1, '2025-12-29 09:21:40', '2025-12-29 09:21:40');
INSERT INTO `station` VALUES (2, '上海黄浦江监测站', '上海市黄浦区', 121.4737, 31.2304, 3.2, 3.8, 1, '2025-12-29 09:21:40', '2025-12-29 09:21:40');
INSERT INTO `station` VALUES (3, '广州珠江监测站', '广州市天河区', 113.2644, 23.1291, 2.8, 3.5, 1, '2025-12-29 09:21:40', '2025-12-29 09:21:40');
INSERT INTO `station` VALUES (4, '深圳深圳河监测站', '深圳市福田区', 114.0579, 22.5431, 2.5, 3, 0, '2025-12-29 09:21:40', '2025-12-29 09:21:40');
INSERT INTO `station` VALUES (5, '武汉长江监测站', '武汉市江岸区', 114.299, 30.594, 3.8, 4.5, 1, '2025-12-29 09:21:40', '2025-12-29 09:21:40');
INSERT INTO `station` VALUES (6, '成都锦江监测站', '成都市锦江区', 104.066, 30.657, 2.2, 2.8, 1, '2025-12-29 09:21:40', '2025-12-29 09:21:40');
INSERT INTO `station` VALUES (7, '杭州钱塘江监测站', '杭州市上城区', 120.155, 30.274, 3, 3.6, 1, '2025-12-29 09:21:40', '2025-12-29 09:21:40');
INSERT INTO `station` VALUES (8, '南京秦淮河监测站', '南京市秦淮区', 118.786, 32.033, 2.6, 3.2, 1, '2025-12-29 09:21:40', '2025-12-29 09:21:40');

SET FOREIGN_KEY_CHECKS = 1;
