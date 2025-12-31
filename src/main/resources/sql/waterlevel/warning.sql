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

 Date: 31/12/2025 11:24:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for warning
-- ----------------------------
DROP TABLE IF EXISTS `warning`;
CREATE TABLE `warning`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `station_id` bigint NOT NULL COMMENT '站点ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '预警标题',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预警内容',
  `level` tinyint NOT NULL COMMENT '预警等级：1-一般，2-警告，3-严重',
  `status` tinyint NULL DEFAULT 0 COMMENT '处理状态：0-未处理，1-已处理',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_station_time`(`station_id` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '预警信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of warning
-- ----------------------------
INSERT INTO `warning` VALUES (1, 2, '上海黄浦江水位接近警戒值', '当前水位3.12米，接近警戒水位3.20米，请密切关注', 2, 0, '2025-12-29 08:52:15');
INSERT INTO `warning` VALUES (2, 5, '武汉长江水位超警戒线', '当前水位3.45米，已超过警戒水位3.20米，请加强监测', 3, 0, '2025-12-29 08:37:15');
INSERT INTO `warning` VALUES (3, 4, '深圳深圳河监测站离线', '设备通讯中断超过2小时，请检查设备状态', 1, 0, '2025-12-29 07:22:15');
INSERT INTO `warning` VALUES (4, 1, '北京通惠河水速加快', '流速从1.6m/s上升到1.8m/s，注意观察', 1, 1, '2025-12-29 06:22:15');
INSERT INTO `warning` VALUES (5, 7, '杭州钱塘江水位上涨较快', '30分钟内上涨0.15米，请注意防范', 2, 0, '2025-12-29 09:02:15');
INSERT INTO `warning` VALUES (6, 2, '黄浦江下游流速异常', '下游流速达到2.8m/s，超过正常范围', 2, 0, '2025-12-29 09:07:15');

SET FOREIGN_KEY_CHECKS = 1;
