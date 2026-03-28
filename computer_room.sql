/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : computer_room

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 28/03/2026 22:15:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `gender` tinyint(4) NULL DEFAULT NULL COMMENT '性别（0-女；1-男）',
  `gmt_created` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE COMMENT '用户名唯一索引',
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_gmt_created`(`gmt_created`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for command
-- ----------------------------
DROP TABLE IF EXISTS `command`;
CREATE TABLE `command`  (
  `id` bigint(19) NOT NULL COMMENT '主键ID',
  `cmd_id` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '指令ID',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `device_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '控制类型：1-散热',
  `command` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '命令：1-ON 2-OFF',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态\r\n',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备反控记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for device_option
-- ----------------------------
DROP TABLE IF EXISTS `device_option`;
CREATE TABLE `device_option`  (
  `id` bigint(19) NOT NULL COMMENT '反控ID',
  `device_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ID',
  `action` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作',
  `operator_id` bigint(19) NULL DEFAULT NULL COMMENT '操作人',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备反控记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for environment
-- ----------------------------
DROP TABLE IF EXISTS `environment`;
CREATE TABLE `environment`  (
  `id` bigint(19) NOT NULL COMMENT '环境数据ID',
  `source` tinyint(4) NULL DEFAULT 1 COMMENT '数据来源（1：硬件上报；0：手动记录）',
  `device_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上报设备',
  `temperature` decimal(5, 2) NULL DEFAULT NULL COMMENT '温度',
  `humidity` decimal(5, 2) NULL DEFAULT NULL COMMENT '湿度',
  `gas_ppm` decimal(5, 2) NULL DEFAULT NULL COMMENT '烟雾浓度',
  `gas_status` tinyint(4) NULL DEFAULT NULL COMMENT 'MQ2烟雾AO状态（1：有烟雾；0：无烟雾）',
  `light_status` tinyint(4) NULL DEFAULT NULL COMMENT '光敏电阻数值（1：无光；0：有光）',
  `flame_status` tinyint(4) NULL DEFAULT NULL COMMENT '火焰传感器AO状态（1：有火；0：无火）',
  `light_percentage` decimal(5, 2) NULL DEFAULT NULL COMMENT '光照强度（0-100）',
  `flame_percentage` decimal(5, 2) NULL DEFAULT NULL COMMENT '附近有火焰的百分比（0-100）',
  `alarm_status` tinyint(4) NULL DEFAULT NULL COMMENT '蜂鸣器报警状态（1：报警；0：未报警）',
  `fan_status` tinyint(4) NULL DEFAULT NULL COMMENT '散热设备开关（1：开启；0：关闭）',
  `led_status` tinyint(4) NULL DEFAULT NULL COMMENT 'led开关（1：开启；0：关闭）',
  `gmt_measurement` datetime NULL DEFAULT NULL COMMENT '测量时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '环境监测数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mqtt_receive_cmd_resp
-- ----------------------------
DROP TABLE IF EXISTS `mqtt_receive_cmd_resp`;
CREATE TABLE `mqtt_receive_cmd_resp`  (
  `id` bigint(19) NOT NULL COMMENT 'ID',
  `topic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'topic主题',
  `payload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'payload有效载荷',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备id',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '接收时间',
  `is_deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_receive_time`(`receive_time`) USING BTREE,
  INDEX `idx_device_id`(`device_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'MQTT接收数据-指令回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mqtt_receive_report
-- ----------------------------
DROP TABLE IF EXISTS `mqtt_receive_report`;
CREATE TABLE `mqtt_receive_report`  (
  `id` bigint(19) NOT NULL COMMENT 'ID',
  `topic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'topic主题',
  `payload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'payload有效载荷',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备id',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '接收时间',
  `is_deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_receive_time`(`receive_time`) USING BTREE,
  INDEX `idx_device_id`(`device_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'MQTT接收数据-上报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mqtt_send_cmd
-- ----------------------------
DROP TABLE IF EXISTS `mqtt_send_cmd`;
CREATE TABLE `mqtt_send_cmd`  (
  `id` bigint(19) NOT NULL COMMENT 'ID',
  `topic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'topic主题',
  `payload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'payload有效载荷',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '设备id',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `is_deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_receive_time`(`send_time`) USING BTREE,
  INDEX `idx_device_id`(`device_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'MQTT发送记录-指令表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
