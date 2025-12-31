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

 Date: 31/12/2025 10:58:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rescue_material_category
-- ----------------------------
DROP TABLE IF EXISTS `rescue_material_category`;
CREATE TABLE `rescue_material_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父级分类ID',
  `mode_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属模式：FLOOD-防汛, DROUGHT-抗旱',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_mode_type`(`mode_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '救援物资分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rescue_material_category
-- ----------------------------
INSERT INTO `rescue_material_category` VALUES (1, '救生转移', 0, 'FLOOD', '把人先接出来', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (2, '排水照明', 0, 'FLOOD', '抢电抢排抢亮', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (3, '生活安置', 0, 'FLOOD', '到安置点就能住', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (4, '医疗防疫', 0, 'FLOOD', '防止灾后疫', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (5, '找水运水', 0, 'DROUGHT', '先解决有水喝', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (6, '节水保墒', 0, 'DROUGHT', '让作物省着喝', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (7, '生活应急', 0, 'DROUGHT', '保人基本生活', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (8, '生产自救', 0, 'DROUGHT', '灾后还能种还能养', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (11, '船艇', 1, 'FLOOD', '橡皮艇、冲锋舟等', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (12, '个人救生', 1, 'FLOOD', '救生衣、救生圈等', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (13, '高空/绳索', 1, 'FLOOD', '安全带、静力绳等', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (21, '发电', 2, 'FLOOD', '发电机组等', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (22, '排水', 2, 'FLOOD', '潜水泵、抽水泵等', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (31, '住宿', 3, 'FLOOD', '帐篷、折叠床等', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (32, '饮食', 3, 'FLOOD', '瓶装水、方便食品等', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (41, '外伤', 4, 'FLOOD', '急救包等', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (42, '消杀', 4, 'FLOOD', '消毒液等', '2025-12-25 15:10:35', '2025-12-25 15:10:35');
INSERT INTO `rescue_material_category` VALUES (51, '找水设备', 5, 'DROUGHT', '找水仪、钻机等', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (52, '抽水设备', 5, 'DROUGHT', '深井泵、潜水泵等', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (53, '运水净水', 5, 'DROUGHT', '运水车、净水机等', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (61, '高效灌溉', 6, 'DROUGHT', '滴灌带、微喷头等', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (62, '农艺保墒', 6, 'DROUGHT', '保水剂、地膜等', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (71, '饮水食品', 7, 'DROUGHT', '瓶装水、压缩饼干等', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (72, '住宿防暑', 7, 'DROUGHT', '帐篷、防暑药品等', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (81, '补种改种', 8, 'DROUGHT', '耐旱种子等', '2025-12-25 15:21:41', '2025-12-25 15:21:41');
INSERT INTO `rescue_material_category` VALUES (82, '畜牧减损', 8, 'DROUGHT', '青贮剂、饮水器等', '2025-12-25 15:21:41', '2025-12-25 15:21:41');

SET FOREIGN_KEY_CHECKS = 1;
