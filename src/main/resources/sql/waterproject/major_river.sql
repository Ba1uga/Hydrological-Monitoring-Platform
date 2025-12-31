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

 Date: 31/12/2025 10:58:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for major_river
-- ----------------------------
DROP TABLE IF EXISTS `major_river`;
CREATE TABLE `major_river`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '河流名称',
  `points` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '坐标点(JSON格式)',
  `color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示颜色',
  `width` int NULL DEFAULT NULL COMMENT '线宽',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '主要河流表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of major_river
-- ----------------------------
INSERT INTO `major_river` VALUES (1, '长江', '[[91.1,35.0],[94.5,33.5],[100.0,32.0],[103.0,30.0],[104.5,29.0],[106.5,28.0],[108.0,29.0],[110.0,30.0],[112.0,30.5],[114.0,30.0],[116.0,30.5],[118.0,31.5],[120.0,31.5],[121.5,31.0]]', '#00ff88', 4);
INSERT INTO `major_river` VALUES (2, '黄河', '[[95.8,36.0],[97.5,34.5],[100.0,36.0],[102.0,36.5],[104.0,36.0],[106.5,34.5],[108.5,34.0],[110.0,35.0],[111.5,36.5],[112.5,36.0],[113.5,35.0],[114.5,34.0],[116.0,33.0],[117.5,35.0],[118.5,37.5],[119.5,38.0]]', '#ffd700', 3);
INSERT INTO `major_river` VALUES (3, '珠江', '[[104.0,25.0],[106.0,24.0],[108.0,23.5],[110.0,22.5],[112.0,22.0],[113.5,23.0],[115.0,22.5]]', '#4dabf7', 3);
INSERT INTO `major_river` VALUES (4, '淮河', '[[113.5,32.0],[115.0,32.5],[116.5,32.0],[118.0,32.5],[119.5,33.5],[120.5,34.0]]', '#ff9800', 2);
INSERT INTO `major_river` VALUES (5, '汉江', '[[107.5,33.5],[109.0,32.5],[110.5,31.5],[112.0,30.5],[113.0,30.0]]', '#80d4ff', 2);
INSERT INTO `major_river` VALUES (6, '金沙江', '[[91.0,33.0],[94.0,30.0],[97.0,28.0],[100.0,26.5],[102.5,26.0],[104.5,26.5]]', '#9c27b0', 3);
INSERT INTO `major_river` VALUES (7, '滦河', '[[115.5,41.5],[117.0,40.5],[118.5,39.5],[119.5,39.0]]', '#0096ff', 2);

SET FOREIGN_KEY_CHECKS = 1;
