/*
Navicat MySQL Data Transfer

Source Server         : 腾讯云
Source Server Version : 80027
Source Host           : 82.157.55.18:3306
Source Database       : pna

Target Server Type    : MYSQL
Target Server Version : 80027
File Encoding         : 65001

Date: 2022-02-26 16:25:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `p_admin`
-- ----------------------------
DROP TABLE IF EXISTS `p_admin`;
CREATE TABLE `p_admin` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '邮箱',
  `pass_word` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `create_time` timestamp NOT NULL COMMENT '注册事件',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_unicode_ci;


-- ----------------------------
-- Table structure for `p_task`
-- ----------------------------
DROP TABLE IF EXISTS `p_task`;
CREATE TABLE `p_task` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `owner_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '所有者用户名',
  `alg_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '算法名称',
  `status` int NOT NULL COMMENT '任务状态',
  `result` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '运算结果',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `create_time` timestamp NOT NULL COMMENT '任务提交时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin;

