/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50556
 Source Host           : localhost:3306
 Source Schema         : user_center

 Target Server Type    : MySQL
 Target Server Version : 50556
 File Encoding         : 65001

 Date: 28/04/2019 18:28:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for auth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `auth_access_token`;
CREATE TABLE `auth_access_token`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'id',
  `access_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'Access Token',
  `user_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '关联的用户ID',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `client_id` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接入的客户端ID',
  `expires_in` bigint(11) NOT NULL DEFAULT 0 COMMENT '过期时间戳',
  `grant_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '授权类型，比如：authorization_code',
  `scope` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '可被访问的用户的权限范围，比如：basic、super',
  `create_user` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建用户',
  `update_user` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最后更新用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Access Token信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of auth_access_token
-- ----------------------------
INSERT INTO `auth_access_token` VALUES ('1', '1.6659c9d38f5943f97db334874e5229284cdd1523.2592000.1537600367', '1', 'admin', '1', 1537600367, 'authorization_code', 'basic', '1', '1', '2018-08-20 14:27:59', '2018-08-23 15:12:47');
INSERT INTO `auth_access_token` VALUES ('2', '1.854373728dbcc35f6f56c7671823bf5e49042f34.2592000.1537600978', '2', 'zifangsky', '2', 1537600978, 'authorization_code', 'super', '2', '2', '2018-08-23 14:08:07', '2018-08-23 15:22:59');

-- ----------------------------
-- Table structure for auth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `auth_client_details`;
CREATE TABLE `auth_client_details`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接入的第三方客户端详情表id',
  `client_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接入的客户端ID',
  `client_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '接入的客户端名称',
  `client_secret` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接入的客户端的密钥',
  `redirect_uri` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '回调地址',
  `description` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述信息',
  `status` int(2) NULL DEFAULT 0 COMMENT '0表示未开通；1表示正常使用；2表示已被禁用',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建用户',
  `update_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最后更新用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '接入的客户端信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for auth_client_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_client_user`;
CREATE TABLE `auth_client_user`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户对某个接入客户端的授权信息表id',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '关联的用户ID',
  `auth_client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '关联的客户端ID',
  `auth_scope_id` varchar(46) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '关联的用户信息范围ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户对某个接入客户端的授权信息' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for auth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `auth_refresh_token`;
CREATE TABLE `auth_refresh_token`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'id',
  `token_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '表auth_access_token对应的Access Token记录',
  `refresh_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'Refresh Token',
  `expires_in` bigint(11) NOT NULL DEFAULT 0 COMMENT '过期时间戳',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建用户',
  `update_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最后更新用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Refresh Token信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of auth_refresh_token
-- ----------------------------
INSERT INTO `auth_refresh_token` VALUES ('1', '1', '2.b19923a01cf35ccab48ddbd687750408bd1cb763.31536000.1566544316', 1566544316, '1', '1', '2018-08-20 14:50:27', '2018-08-23 15:11:57');
INSERT INTO `auth_refresh_token` VALUES ('2', '2', '2.cb8a3e903eecd6b74fc33c111b4a286099ace3ce.31536000.1566544978', 1566544978, '2', '2', '2018-08-23 14:08:07', '2018-08-23 15:22:59');

-- ----------------------------
-- Table structure for auth_scope
-- ----------------------------
DROP TABLE IF EXISTS `auth_scope`;
CREATE TABLE `auth_scope`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '用户信息范围表id',
  `scope_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '可被访问的用户的权限范围，比如：basic、super',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '可被访问的用户权限表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of auth_scope
-- ----------------------------
INSERT INTO `auth_scope` VALUES ('1', 'super', '用户所有信息');
INSERT INTO `auth_scope` VALUES ('2', 'basic', '用户基本信息');

-- ----------------------------
-- Table structure for sso_access_token
-- ----------------------------
DROP TABLE IF EXISTS `sso_access_token`;
CREATE TABLE `sso_access_token`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'id',
  `access_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'Access Token',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '关联的用户ID',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户名',
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户来源IP',
  `client_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '关联的客户端ID',
  `channel` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '表示这个Token用于什么渠道，比如：官网、APP1、APP2等等',
  `expires_in` bigint(11) NOT NULL DEFAULT 0 COMMENT '过期时间戳',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建用户',
  `update_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最后更新用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '单点登录的Access Token信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sso_access_token
-- ----------------------------
INSERT INTO `sso_access_token` VALUES ('1', '11.0e7baee3e290429b54a5692a4eee8af5f99a9862.2592000.1538210962', '1', 'admin', '127.0.0.1', '2', 'APP1', 1538210962, '1', '1', '2018-08-30 16:22:00', '2018-08-30 16:49:23');
INSERT INTO `sso_access_token` VALUES ('2', '11.eb9866fa4f6dd1d4d32acb72e3db33502a7541ed.2592000.1538536378', '1', 'admin', '127.0.0.1', '1', 'TEST_CLIENT1', 1538536378, '1', '1', '2018-08-30 17:33:26', '2018-09-03 11:12:58');
INSERT INTO `sso_access_token` VALUES ('3', '11.6ce7388a0d37c81c2a0c5661dbb94e1670a81e34.2592000.1538213738', '1', 'admin', '127.0.0.1', '3', 'APP2', 1538213738, '1', '1', '2018-08-30 17:35:38', '2018-08-30 17:35:38');
INSERT INTO `sso_access_token` VALUES ('4', '11.c40f4990d25fd30670d0171bc99625d3aef134d0.2592000.1538288980', '2', 'zifangsky', '127.0.0.1', '1', 'TEST_CLIENT1', 1538288980, '2', '2', '2018-08-31 14:11:19', '2018-08-31 14:29:40');

-- ----------------------------
-- Table structure for sso_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sso_client_details`;
CREATE TABLE `sso_client_details`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'id',
  `client_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '接入单点登录的系统名称',
  `description` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '说明',
  `redirect_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '请求Token的回调URL',
  `logout_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '注销URL',
  `status` int(2) NOT NULL DEFAULT 0 COMMENT '0表示未开通；1表示正常使用；2表示已被禁用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '单点登录的回调域名的白名单' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sso_client_details
-- ----------------------------
INSERT INTO `sso_client_details` VALUES ('1', 'TEST_CLIENT1', NULL, 'http://127.0.0.1:7000/user/userIndex', 'http://127.0.0.1:7000/logout', 1);
INSERT INTO `sso_client_details` VALUES ('2', 'APP1', NULL, 'http://127.0.0.1:6080/login', 'http://127.0.0.1:6080/logout', 1);
INSERT INTO `sso_client_details` VALUES ('3', 'APP2', NULL, 'http://192.168.197.130:6080/login', 'http://192.168.197.130:6080/logout', 1);

-- ----------------------------
-- Table structure for sso_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `sso_refresh_token`;
CREATE TABLE `sso_refresh_token`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'id',
  `token_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '表sso_access_token对应的Access Token记录',
  `refresh_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'Refresh Token',
  `expires_in` bigint(11) NOT NULL DEFAULT 0 COMMENT '过期时间戳',
  `create_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建用户',
  `update_user` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '最后更新用户',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '单点登录的Refresh Token信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sso_refresh_token
-- ----------------------------
INSERT INTO `sso_refresh_token` VALUES ('3', '1', '12.143a279cb81b0e5063af9912f346ae16e49c17e2.31536000.1567154963', 1567154963, '1', '1', '2018-08-30 16:22:35', '2018-08-30 16:49:23');
INSERT INTO `sso_refresh_token` VALUES ('4', '2', '12.76ea056a721c7142c3a5c48d3f1e73f627c94c2e.31536000.1567231591', 1567231591, '1', '1', '2018-08-30 17:33:27', '2018-08-31 14:06:32');
INSERT INTO `sso_refresh_token` VALUES ('5', '3', '12.08d739a43784b1b155cf26f44bbf082f91810727.31536000.1567157738', 1567157738, '1', '1', '2018-08-30 17:35:39', '2018-08-30 17:35:39');
INSERT INTO `sso_refresh_token` VALUES ('6', '4', '12.62f4e8fd4a41e2bf075979d3639fd9a637999548.31536000.1567232980', 1567232980, '2', '2', '2018-08-31 14:11:19', '2018-08-31 14:29:40');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户表id',
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户昵称',
  `avatar` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '用户头像',
  `phone` int(11) NULL DEFAULT 0 COMMENT '用户手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `question` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '找回密码的问题',
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '找回密码的答案',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user_auth
-- ----------------------------
DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户登录授权表id',
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户id',
  `identity_type` int(6) NOT NULL DEFAULT 0 COMMENT '登录类型（手机号 邮箱 用户名）或第三方应用名称（微信 微博等）',
  `identifier` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标识（手机号 邮箱 用户名或第三方应用的唯一标识）',
  `credential` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码凭证（站内的保存密码，站外的不保存或保存token）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
