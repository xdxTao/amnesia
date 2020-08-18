/*
 Navicat Premium Data Transfer

 Source Server         : 47.100.60.252
 Source Server Type    : MySQL
 Source Server Version : 50564
 Source Host           : 47.100.60.252:9389
 Source Schema         : amnesia_notepad

 Target Server Type    : MySQL
 Target Server Version : 50564
 File Encoding         : 65001

 Date: 18/08/2020 20:25:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sy_label
-- ----------------------------
DROP TABLE IF EXISTS `sy_label`;
CREATE TABLE `sy_label`  (
  `label_id` int(6) NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `user_id` int(6) NOT NULL COMMENT '用户id',
  `label_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签名',
  `label_sort` tinyint(3) NOT NULL DEFAULT 0 COMMENT '标签排序',
  `label_status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '是否启用（1，启用，0停用）',
  `label_default` tinyint(3) NOT NULL DEFAULT 0 COMMENT '默认标签（1，默认）',
  PRIMARY KEY (`label_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '标签表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for sy_user
-- ----------------------------
DROP TABLE IF EXISTS `sy_user`;
CREATE TABLE `sy_user`  (
  `user_id` int(6) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `wx_open_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '微信openId',
  `user_status` tinyint(3) NOT NULL COMMENT '用户状态:1启用0停用',
  `is_authorize` tinyint(3) NOT NULL DEFAULT 0 COMMENT '是否授权1已授权0未授权',
  `gender` tinyint(3) NOT NULL DEFAULT 0 COMMENT '用户性别 0 未知、1 男性，2 女性',
  `user_remarks` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
