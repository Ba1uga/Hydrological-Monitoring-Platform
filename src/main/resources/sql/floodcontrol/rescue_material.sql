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

 Date: 31/12/2025 10:58:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rescue_material
-- ----------------------------
DROP TABLE IF EXISTS `rescue_material`;
CREATE TABLE `rescue_material`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_id` bigint NOT NULL COMMENT '物资分类ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物资名称',
  `specification` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计量单位',
  `total_quantity` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总数量',
  `available_quantity` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '可用数量',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1-正常, 0-停用, 2-维护中',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '存放位置/仓库',
  `last_check_time` datetime NULL DEFAULT NULL COMMENT '最后盘点时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '救援物资表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rescue_material
-- ----------------------------
INSERT INTO `rescue_material` VALUES (1, 11, '冲锋舟', '6人座', '艘', 20.00, 15.00, 1, '1号仓库', NULL, '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material` VALUES (2, 11, '橡皮艇', '4人座', '艘', 50.00, 48.00, 1, '1号仓库', NULL, '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material` VALUES (3, 12, '救生衣', '通用型', '件', 1000.00, 800.00, 1, '2号仓库', NULL, '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material` VALUES (4, 21, '柴油发电机', '30kW', '台', 10.00, 8.00, 1, '3号仓库', NULL, '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material` VALUES (5, 22, '大流量潜水泵', '300m³/h', '台', 20.00, 18.00, 1, '3号仓库', NULL, '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material` VALUES (6, 31, '双人帐篷', '常规', '顶', 500.00, 450.00, 1, '4号仓库', NULL, '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material` VALUES (7, 42, '84消毒液', '500ml/瓶', '箱', 200.00, 180.00, 1, '5号仓库', NULL, '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material` VALUES (8, 11, '冲锋舟', '6人座', '艘', 20.00, 15.00, 1, '1号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (9, 11, '橡皮艇', '4人座', '艘', 50.00, 48.00, 1, '1号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (10, 12, '救生衣', '通用型', '件', 1000.00, 800.00, 1, '2号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (11, 21, '柴油发电机', '30kW', '台', 10.00, 8.00, 1, '3号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (12, 22, '大流量潜水泵', '300m³/h', '台', 20.00, 18.00, 1, '3号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (13, 31, '双人帐篷', '常规', '顶', 500.00, 450.00, 1, '4号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (14, 42, '84消毒液', '500ml/瓶', '箱', 200.00, 180.00, 1, '5号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (15, 51, '物探找水仪', '高精度', '台', 5.00, 5.00, 1, '6号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (16, 52, '深井泵', '高扬程', '台', 30.00, 25.00, 1, '6号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (17, 53, '运水车', '10吨', '辆', 15.00, 12.00, 1, '车辆中心', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (18, 61, '滴灌带', '内镶式', '米', 10000.00, 8000.00, 1, '7号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (19, 62, '抗旱保水剂', '聚丙烯酰胺', 'kg', 500.00, 400.00, 1, '7号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (20, 71, '瓶装水', '500ml*24', '箱', 2000.00, 1500.00, 1, '8号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material` VALUES (21, 81, '耐旱玉米种子', '短生育期', 'kg', 1000.00, 900.00, 1, '9号仓库', NULL, '2025-12-25 15:21:41', '2025-12-25 15:21:41');

SET FOREIGN_KEY_CHECKS = 1;
