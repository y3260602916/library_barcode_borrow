/*
 Navicat Premium Dump SQL

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : library_db

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 08/06/2026 11:40:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book_info
-- ----------------------------
DROP TABLE IF EXISTS `book_info`;
CREATE TABLE `book_info`  (
  `book_id` int NOT NULL AUTO_INCREMENT COMMENT '图书ID，主键自增',
  `book_barcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图书条码/编号，AI识别目标',
  `book_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图书名称',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
  `category` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图书分类',
  `total_stock` int NOT NULL DEFAULT 0 COMMENT '总库存',
  `remain_stock` int NOT NULL DEFAULT 0 COMMENT '剩余可借库存',
  `price` decimal(8, 2) NULL DEFAULT NULL COMMENT '图书价格',
  PRIMARY KEY (`book_id`) USING BTREE,
  UNIQUE INDEX `book_barcode`(`book_barcode` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_info
-- ----------------------------
INSERT INTO `book_info` VALUES (1, '9787115546081', 'Python编程入门', '张三', '计算机', 10, 8, 59.80);
INSERT INTO `book_info` VALUES (2, '9787111636663', 'Java核心技术', '李四', '计算机', 8, 5, 79.00);
INSERT INTO `book_info` VALUES (3, '9787020002207', '红楼梦', '曹雪芹', '文学', 15, 12, 45.50);

-- ----------------------------
-- Table structure for borrow_record
-- ----------------------------
DROP TABLE IF EXISTS `borrow_record`;
CREATE TABLE `borrow_record`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '借阅记录ID',
  `user_id` int NOT NULL COMMENT '关联用户ID',
  `book_id` int NOT NULL COMMENT '关联图书ID',
  `borrow_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借阅时间',
  `return_time` datetime NULL DEFAULT NULL COMMENT '归还时间，为空表示未归还',
  `is_overdue` tinyint NULL DEFAULT 0 COMMENT '是否逾期：0=正常 1=逾期',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE,
  CONSTRAINT `borrow_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `borrow_record_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `book_info` (`book_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书借阅记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow_record
-- ----------------------------

-- ----------------------------
-- Table structure for scan_record
-- ----------------------------
DROP TABLE IF EXISTS `scan_record`;
CREATE TABLE `scan_record`  (
  `scan_id` int NOT NULL AUTO_INCREMENT COMMENT '识别记录ID',
  `img_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上传图片本地路径',
  `scan_barcode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'AI识别出的条码编号',
  `scan_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '识别时间',
  `result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '识别结果：成功/失败',
  PRIMARY KEY (`scan_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI条码识别日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scan_record
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键自增',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `user_account` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号/登录账号，唯一',
  `user_type` tinyint NOT NULL COMMENT '用户类型：0=学生 1=管理员',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_account`(`user_account` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '师生与管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '张三', '2026001', 0, '2026-06-08 11:17:18');
INSERT INTO `sys_user` VALUES (2, '李四', '2026002', 0, '2026-06-08 11:17:18');
INSERT INTO `sys_user` VALUES (3, '管理员', 'admin', 1, '2026-06-08 11:17:18');

SET FOREIGN_KEY_CHECKS = 1;
