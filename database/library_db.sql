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

 Date: 14/06/2026 11:40:15
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
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_info
-- ----------------------------
INSERT INTO `book_info` VALUES (1, '9787115546081', 'Python编程入门', '张三', '计算机', 10, 8, 59.80);
INSERT INTO `book_info` VALUES (2, '9787111636663', 'Java核心技术', '李四', '计算机', 8, 5, 79.00);
INSERT INTO `book_info` VALUES (3, '9787020002207', '红楼梦', '曹雪芹', '文学', 15, 11, 45.50);
INSERT INTO `book_info` VALUES (4, '9787553811550', '测试图书（条码：9787553811550）', '佚名', '测试类', 10, 10, 39.80);
INSERT INTO `book_info` VALUES (5, '9787121350337', 'Docker微服务架构实战', '蒋彪', '计算机', 1, 1, 69.00);
INSERT INTO `book_info` VALUES (6, '9787801791320', '死魂灵', '果戈里', '文学类', 4, 1, 29.00);
INSERT INTO `book_info` VALUES (45, '9787530212345', '三体：黑暗森林', '刘慈欣', '科幻', 20, 15, 68.00);
INSERT INTO `book_info` VALUES (46, '9787544291170', '百年孤独', '加西亚·马尔克斯', '文学', 12, 10, 55.00);
INSERT INTO `book_info` VALUES (47, '9787115412348', '算法导论', 'Thomas H. Cormen', '计算机', 5, 2, 128.00);
INSERT INTO `book_info` VALUES (48, '9787121274281', '深入理解Java虚拟机', '周志明', '计算机', 6, 4, 99.00);
INSERT INTO `book_info` VALUES (49, '9787501523412', '明朝那些事儿', '当年明月', '历史', 18, 14, 88.00);
INSERT INTO `book_info` VALUES (50, '9787544764501', '追风筝的人', '卡勒德·胡赛尼', '文学', 9, 7, 42.00);
INSERT INTO `book_info` VALUES (51, '9787302145642', '机器学习', '周志华', '计算机', 7, 3, 88.00);
INSERT INTO `book_info` VALUES (52, '9787115345672', 'C++ Primer Plus', 'Stephen Prata', '计算机', 10, 6, 108.00);
INSERT INTO `book_info` VALUES (53, '9787530201235', '围城', '钱钟书', '文学', 8, 5, 39.00);
INSERT INTO `book_info` VALUES (54, '9787560012345', '新概念英语', '亚历山大', '外语', 25, 20, 49.00);
INSERT INTO `book_info` VALUES (55, '9787208061152', '枪炮、病菌与钢铁', '贾雷德·戴蒙德', '历史', 6, 3, 79.00);
INSERT INTO `book_info` VALUES (56, '9787301156112', '经济学原理', 'N.格里高利·曼昆', '经济', 15, 12, 118.00);
INSERT INTO `book_info` VALUES (57, '9787530217845', '白夜行', '东野圭吾', '悬疑', 14, 11, 49.80);
INSERT INTO `book_info` VALUES (58, '9787201158947', '人类简史', '尤瓦尔·赫拉利', '历史', 10, 8, 68.00);
INSERT INTO `book_info` VALUES (59, '9787208064535', '未来简史', '尤瓦尔·赫拉利', '历史', 8, 6, 68.00);
INSERT INTO `book_info` VALUES (60, '9787544291194', '霍乱时期的爱情', '加西亚·马尔克斯', '文学', 7, 5, 52.00);
INSERT INTO `book_info` VALUES (61, '9787121342455', 'Effective Java', 'Joshua Bloch', '计算机', 5, 2, 99.00);
INSERT INTO `book_info` VALUES (62, '9787301134553', '统计学习方法', '李航', '计算机', 4, 1, 78.00);
INSERT INTO `book_info` VALUES (63, '9787530204567', '活着', '余华', '文学', 12, 10, 35.00);
INSERT INTO `book_info` VALUES (64, '9787544764518', '挪威的森林', '村上春树', '文学', 9, 7, 46.00);
INSERT INTO `book_info` VALUES (65, '9787115479082', '深入浅出数据分析', 'Michael Milton', '计算机', 6, 3, 89.00);
INSERT INTO `book_info` VALUES (66, '9787513312345', '盗墓笔记', '南派三叔', '悬疑', 15, 12, 99.00);
INSERT INTO `book_info` VALUES (67, '9787513314567', '鬼吹灯', '天下霸唱', '悬疑', 14, 11, 95.00);
INSERT INTO `book_info` VALUES (68, '9787544764525', '解忧杂货店', '东野圭吾', '悬疑', 11, 8, 39.80);
INSERT INTO `book_info` VALUES (69, '9787201158954', '今日简史', '尤瓦尔·赫拉利', '历史', 6, 4, 62.00);
INSERT INTO `book_info` VALUES (70, '9787121340079', 'Spring Boot实战', 'Craig Walls', '计算机', 7, 4, 79.00);
INSERT INTO `book_info` VALUES (71, '9787302512123', 'Python网络爬虫从入门到实践', '唐松', '计算机', 9, 6, 59.00);
INSERT INTO `book_info` VALUES (72, '9787530210129', '平凡的世界', '路遥', '文学', 20, 14, 88.00);
INSERT INTO `book_info` VALUES (73, '9787544764532', '白夜行（精装版）', '东野圭吾', '悬疑', 5, 2, 68.00);
INSERT INTO `book_info` VALUES (74, '9787201158961', '丝绸之路', '彼得·弗兰科潘', '历史', 5, 3, 128.00);
INSERT INTO `book_info` VALUES (75, '9787121330285', 'MySQL必知必会', 'Ben Forta', '计算机', 12, 8, 49.00);
INSERT INTO `book_info` VALUES (76, '9787513316789', '三体：死神永生', '刘慈欣', '科幻', 18, 14, 68.00);
INSERT INTO `book_info` VALUES (77, '9787544291187', '雪国', '川端康成', '文学', 6, 4, 38.00);
INSERT INTO `book_info` VALUES (78, '9787301142565', '线性代数及其应用', 'David C. Lay', '数学', 8, 5, 68.00);

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
  `fine_money` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '逾期罚金',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `book_id`(`book_id` ASC) USING BTREE,
  CONSTRAINT `borrow_record_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `borrow_record_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `book_info` (`book_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图书借阅记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of borrow_record
-- ----------------------------
INSERT INTO `borrow_record` VALUES (1, 1, 4, '2026-06-09 09:00:39', '2026-06-09 09:22:33', 0, 0.00);
INSERT INTO `borrow_record` VALUES (2, 1, 4, '2026-06-09 11:27:40', '2026-06-09 15:44:17', 0, 0.00);
INSERT INTO `borrow_record` VALUES (4, 1, 6, '2026-06-11 09:39:43', '2026-06-11 09:39:54', 0, 0.00);
INSERT INTO `borrow_record` VALUES (5, 1, 6, '2026-06-11 09:44:23', '2026-06-11 09:44:35', 0, 0.00);
INSERT INTO `borrow_record` VALUES (6, 1, 6, '2026-06-11 09:44:55', '2026-06-11 09:44:59', 0, 0.00);
INSERT INTO `borrow_record` VALUES (7, 1, 6, '2026-06-11 09:59:23', '2026-06-11 09:59:26', 0, 0.00);
INSERT INTO `borrow_record` VALUES (8, 1, 6, '2026-06-11 09:59:31', '2026-06-11 09:59:39', 0, 0.00);
INSERT INTO `borrow_record` VALUES (9, 1, 6, '2026-06-11 10:08:23', '2026-06-11 10:08:32', 0, 0.00);
INSERT INTO `borrow_record` VALUES (10, 1, 6, '2026-06-11 10:13:36', '2026-06-11 10:13:44', 0, 0.00);
INSERT INTO `borrow_record` VALUES (11, 1, 6, '2026-06-11 10:14:28', '2026-06-11 10:14:56', 0, 0.00);
INSERT INTO `borrow_record` VALUES (12, 1, 6, '2026-06-11 11:15:12', '2026-06-11 11:15:58', 0, 0.00);
INSERT INTO `borrow_record` VALUES (13, 1, 6, '2026-06-11 11:16:14', '2026-06-11 11:16:40', 0, 0.00);
INSERT INTO `borrow_record` VALUES (14, 1, 6, '2026-06-11 11:17:18', '2026-06-11 11:18:19', 0, 0.00);
INSERT INTO `borrow_record` VALUES (15, 1, 6, '2026-06-11 14:20:23', '2026-06-11 14:57:35', 0, 0.00);
INSERT INTO `borrow_record` VALUES (16, 1, 5, '2026-06-11 14:20:33', '2026-06-11 14:21:05', 0, 0.00);
INSERT INTO `borrow_record` VALUES (17, 1, 6, '2026-06-11 14:58:13', '2026-06-11 14:58:46', 0, 0.00);
INSERT INTO `borrow_record` VALUES (18, 1, 6, '2026-06-14 08:47:09', '2026-06-14 08:47:13', 0, 0.00);
INSERT INTO `borrow_record` VALUES (19, 1, 6, '2026-06-14 09:20:10', NULL, 0, 0.00);
INSERT INTO `borrow_record` VALUES (20, 4, 6, '2026-06-14 09:41:43', '2026-06-14 09:42:46', 0, 0.00);
INSERT INTO `borrow_record` VALUES (21, 4, 6, '2026-06-14 09:43:53', '2026-06-14 09:44:51', 0, 0.00);
INSERT INTO `borrow_record` VALUES (22, 4, 72, '2026-06-14 10:06:48', NULL, 0, 0.00);
INSERT INTO `borrow_record` VALUES (23, 4, 6, '2026-06-14 10:13:01', '2026-06-14 10:28:42', 0, 0.00);
INSERT INTO `borrow_record` VALUES (24, 4, 6, '2026-06-14 10:29:02', '2026-06-14 10:36:49', 0, 0.00);
INSERT INTO `borrow_record` VALUES (25, 4, 6, '2026-06-14 10:38:59', '2026-06-14 10:39:29', 0, 0.00);
INSERT INTO `borrow_record` VALUES (26, 4, 6, '2026-06-14 10:46:19', '2026-06-14 10:46:50', 0, 0.00);
INSERT INTO `borrow_record` VALUES (27, 4, 3, '2026-06-14 10:46:22', NULL, 0, 0.00);

-- ----------------------------
-- Table structure for inventory_record
-- ----------------------------
DROP TABLE IF EXISTS `inventory_record`;
CREATE TABLE `inventory_record`  (
  `record_id` int NOT NULL AUTO_INCREMENT,
  `book_id` int NOT NULL,
  `book_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` tinyint NOT NULL COMMENT '1=入库 2=出库',
  `quantity` int NOT NULL,
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `operate_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of inventory_record
-- ----------------------------
INSERT INTO `inventory_record` VALUES (1, 6, '死魂灵', 1, 1, '管理员', '2026-06-14 08:30:18', '');

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
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI条码识别日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scan_record
-- ----------------------------
INSERT INTO `scan_record` VALUES (1, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a04f6a5b-9e18-4986-aeb6-4e6d83607d10_20260609082855.jpg', '9787553811550', '2026-06-09 08:28:58', '成功');
INSERT INTO `scan_record` VALUES (2, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\d9213ec2-86f0-4575-a9b1-fb5f4fb5772d_20260609082904.jpg', '9787553811550', '2026-06-09 08:29:06', '成功');
INSERT INTO `scan_record` VALUES (3, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\d837a592-9547-428e-be61-bd86b5deeb43_20260609112041.jpg', '9787553811550', '2026-06-09 11:20:44', '成功');
INSERT INTO `scan_record` VALUES (4, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\5613d1d9-4210-4194-9c0a-de9aac88de0c_20260609112147.jpg', '9787553811550', '2026-06-09 11:21:50', '成功');
INSERT INTO `scan_record` VALUES (5, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\75449a97-4828-4033-8c15-42967d2a38d4_20260609112510.jpg', '9787553811550', '2026-06-09 11:25:14', '成功');
INSERT INTO `scan_record` VALUES (6, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\ba291fab-7b1e-4272-bb69-479c455e5278_20260609112558.jpg', '9787553811550', '2026-06-09 11:26:01', '成功');
INSERT INTO `scan_record` VALUES (7, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\e12dbedb-dff9-4c6e-b850-a1d2aaef3bc3_20260609112723.jpg', '9787553811550', '2026-06-09 11:27:26', '成功');
INSERT INTO `scan_record` VALUES (8, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\28d49d5b-6ae8-4756-a904-0230f371ed62_20260609153005.jpg', '9787553811550', '2026-06-09 15:30:08', '成功');
INSERT INTO `scan_record` VALUES (9, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\3c8d3640-58b7-48f3-bb32-73794a117bd9_20260609154005.jpg', '9787553811550', '2026-06-09 15:40:09', '成功');
INSERT INTO `scan_record` VALUES (10, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\04bdf30c-17c6-41ef-9ea4-219ea0006f14_20260609155556.png', '', '2026-06-09 15:56:00', '失败');
INSERT INTO `scan_record` VALUES (11, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\3acaaf6c-effd-46ab-930e-571a48db2a31_20260609155608.png', '', '2026-06-09 15:56:12', '失败');
INSERT INTO `scan_record` VALUES (12, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\39427f48-7ad5-43ae-9d4c-df9f591da092_20260609155611.png', '', '2026-06-09 15:56:18', '失败');
INSERT INTO `scan_record` VALUES (13, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\674b24a5-b2d7-46aa-b623-17666080ad3e_20260609155614.png', '', '2026-06-09 15:56:19', '失败');
INSERT INTO `scan_record` VALUES (14, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\53abedfc-312e-4e91-91ae-bb60765207a5_20260609155639.png', '', '2026-06-09 15:56:42', '失败');
INSERT INTO `scan_record` VALUES (15, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\12fa36ec-3374-461c-be49-5311ac6aed51_20260609155642.png', '02266813', '2026-06-09 15:56:45', '成功');
INSERT INTO `scan_record` VALUES (16, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\aa47b82e-ace2-43de-bfdb-fd0394a80162_20260609155711.png', '', '2026-06-09 15:57:14', '失败');
INSERT INTO `scan_record` VALUES (17, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\4a9dec87-c700-446a-b33b-f1748f2d8c4e_20260609155713.png', '', '2026-06-09 15:57:17', '失败');
INSERT INTO `scan_record` VALUES (18, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1415b27a-13b5-413b-b023-683ea831a35d_20260609155714.png', '', '2026-06-09 15:57:17', '失败');
INSERT INTO `scan_record` VALUES (19, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\873745c3-ba15-4e71-a032-22f265f1d277_20260609155715.png', '71111629', '2026-06-09 15:57:19', '成功');
INSERT INTO `scan_record` VALUES (20, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\430d0e40-f101-4b0e-bba6-6615ecc6c96b_20260609160813.png', '24423809547493100100', '2026-06-09 16:08:17', '成功');
INSERT INTO `scan_record` VALUES (21, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\e1f6389d-b68e-4918-9c06-d2b30350f749_20260609160903.png', '2432386935100100', '2026-06-09 16:09:07', '成功');
INSERT INTO `scan_record` VALUES (22, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\6255291f-fff3-4329-a936-a4c393dc67d7_20260609160947.png', '2462116683502100100', '2026-06-09 16:09:51', '成功');
INSERT INTO `scan_record` VALUES (23, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\852cefca-d85f-47a4-a12d-4999116dd203_20260609162021.png', '3222', '2026-06-09 16:20:22', '成功');
INSERT INTO `scan_record` VALUES (24, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\0f304870-f02d-4105-ad67-4ecd62fcdc30_20260609162046.png', '3222', '2026-06-09 16:20:46', '成功');
INSERT INTO `scan_record` VALUES (25, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\06f8fbb5-7ad6-484d-b6df-18baa792a160_20260611081844.png', '3222', '2026-06-11 08:18:45', '成功');
INSERT INTO `scan_record` VALUES (26, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\22e24247-823b-4281-b2c8-2a43b047b367_20260611082339.png', '3222', '2026-06-11 08:23:41', '成功');
INSERT INTO `scan_record` VALUES (27, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a9c9777b-9d68-4d5e-87e6-5084bce1ae90_20260611083731.jpg', '2', '2026-06-11 08:37:32', '成功');
INSERT INTO `scan_record` VALUES (28, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\8ad99add-7975-496e-a7ee-2d68f2755619_20260611091119.jpg', '', '2026-06-11 09:11:21', '失败');
INSERT INTO `scan_record` VALUES (29, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\0a65ac79-8ee4-4e3c-adab-92c9130bf811_20260611091617.jpg', '', '2026-06-11 09:16:19', '失败');
INSERT INTO `scan_record` VALUES (30, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\e929e844-661c-4f36-bf19-213d60e4fbd6_20260611092018.jpg', '9787801791320', '2026-06-11 09:20:20', '成功');
INSERT INTO `scan_record` VALUES (31, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\568fe603-f017-4de0-89d2-3876b5022017_20260611092200.jpg', '', '2026-06-11 09:22:03', '失败');
INSERT INTO `scan_record` VALUES (32, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1830ffd4-701e-4da7-a0ca-d0ed39e9003a_20260611092215.jpg', '9787801791320', '2026-06-11 09:22:17', '成功');
INSERT INTO `scan_record` VALUES (33, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\3569ae64-2b93-4c7d-9e91-131e039ea91c_20260611093900.jpg', '', '2026-06-11 09:39:02', '失败');
INSERT INTO `scan_record` VALUES (34, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\d4c6e688-0d0b-40f4-84f7-66695cf3c579_20260611093913.jpg', '', '2026-06-11 09:39:15', '失败');
INSERT INTO `scan_record` VALUES (35, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\2b4fb687-c568-476f-862d-44dd2f3af721_20260611093921.jpg', '', '2026-06-11 09:39:23', '失败');
INSERT INTO `scan_record` VALUES (36, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\7ca027e3-fee6-4ba9-a26f-0e263a49f375_20260611093930.jpg', '', '2026-06-11 09:39:33', '失败');
INSERT INTO `scan_record` VALUES (37, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1fb92acc-82c1-4514-9f09-7c540535ebfd_20260611093937.jpg', '9787801791320', '2026-06-11 09:39:39', '成功');
INSERT INTO `scan_record` VALUES (38, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\036d237c-9a2e-43ff-84cc-1171f9f88e14_20260611095908.jpg', '', '2026-06-11 09:59:10', '失败');
INSERT INTO `scan_record` VALUES (39, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\5bee8462-17e3-462e-ae84-94d0add2f8b6_20260611095915.jpg', '9787801791320', '2026-06-11 09:59:17', '成功');
INSERT INTO `scan_record` VALUES (40, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\0e29993d-861d-4b83-a4fa-a7bf867cca2f_20260611100523.jpg', '9787801791320', '2026-06-11 10:05:26', '成功');
INSERT INTO `scan_record` VALUES (41, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\77ba2053-c20d-457a-994a-e5c71a99949b_20260611101326.jpg', '9787801791320', '2026-06-11 10:13:28', '成功');
INSERT INTO `scan_record` VALUES (42, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\8c93f785-7699-431a-94f5-6c099d46dff8_20260611101357.jpg', '', '2026-06-11 10:14:00', '失败');
INSERT INTO `scan_record` VALUES (43, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\b7590f95-3c5e-486f-84b1-daa3aaee4bd0_20260611101402.jpg', '', '2026-06-11 10:14:04', '失败');
INSERT INTO `scan_record` VALUES (44, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\4bbb96d5-0406-4d63-9e83-47a4a3900d8e_20260611101405.jpg', '711881791320', '2026-06-11 10:14:07', '成功');
INSERT INTO `scan_record` VALUES (45, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1622b896-a539-4fd0-b374-6485a80ab02b_20260611101413.jpg', '', '2026-06-11 10:14:15', '失败');
INSERT INTO `scan_record` VALUES (46, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\b4444c1e-0946-4abf-8833-88e60e96bd03_20260611101416.jpg', '', '2026-06-11 10:14:19', '失败');
INSERT INTO `scan_record` VALUES (47, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\e6ac7326-42d4-46dc-9b6a-1f0ddfbbab5f_20260611101420.jpg', '9787801791320', '2026-06-11 10:14:23', '成功');
INSERT INTO `scan_record` VALUES (48, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1b162a37-a42b-4204-bb38-065505b30136_20260611111215.jpg', '07', '2026-06-11 11:12:19', '成功');
INSERT INTO `scan_record` VALUES (49, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\ea17c90e-9e2a-443f-b543-05a0f279eaf8_20260611111218.jpg', '02', '2026-06-11 11:12:20', '成功');
INSERT INTO `scan_record` VALUES (50, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\67865dde-f709-4493-a479-65184035cf1b_20260611111219.jpg', '9787801791320', '2026-06-11 11:12:21', '成功');
INSERT INTO `scan_record` VALUES (51, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a234d349-9d9a-405a-bccf-9961dab82919_20260611111219.jpg', '9787801791320', '2026-06-11 11:12:21', '成功');
INSERT INTO `scan_record` VALUES (52, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\da1c6792-3550-4d8a-aff3-f41e4d946a0f_20260611111219.jpg', '', '2026-06-11 11:12:22', '失败');
INSERT INTO `scan_record` VALUES (53, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\4a3efcf2-a801-4172-a6f5-01096b93540a_20260611111220.jpg', '02', '2026-06-11 11:12:22', '成功');
INSERT INTO `scan_record` VALUES (54, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1e7d3b69-6669-4394-ae22-5ad6ac9bba3d_20260611111220.jpg', '02', '2026-06-11 11:12:22', '成功');
INSERT INTO `scan_record` VALUES (55, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\012ced76-578a-42b5-a49e-c955de47481e_20260611111220.jpg', '', '2026-06-11 11:12:22', '失败');
INSERT INTO `scan_record` VALUES (56, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\3e002ee4-d891-43e3-adcf-974ce2419aad_20260611111221.jpg', '02', '2026-06-11 11:12:23', '成功');
INSERT INTO `scan_record` VALUES (57, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1d75d34c-4c9b-41c6-b3eb-faeee2e6a13a_20260611111221.jpg', '9787801791320', '2026-06-11 11:12:23', '成功');
INSERT INTO `scan_record` VALUES (58, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\563fc6b0-2ea3-46fb-91fa-e23838b96fdb_20260611111221.jpg', '', '2026-06-11 11:12:23', '失败');
INSERT INTO `scan_record` VALUES (59, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\d32dd390-45d1-4eff-995b-481a39eacd5c_20260611111501.jpg', '9787801791320', '2026-06-11 11:15:03', '成功');
INSERT INTO `scan_record` VALUES (60, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\0a655151-72c9-47eb-9bc6-4d5402e28c05_20260611111549.jpg', '9787801791320', '2026-06-11 11:15:51', '成功');
INSERT INTO `scan_record` VALUES (61, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\549b1239-8b5b-4745-9d04-e4d8757bfe1a_20260611111627.jpg', '', '2026-06-11 11:16:30', '失败');
INSERT INTO `scan_record` VALUES (62, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\86cf5704-250a-4625-bd27-578539eaa64b_20260611111631.jpg', '9787801791320', '2026-06-11 11:16:33', '成功');
INSERT INTO `scan_record` VALUES (63, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\2dd57f3e-fe0c-4533-a604-168aaa61bde6_20260611111755.jpg', '', '2026-06-11 11:17:58', '失败');
INSERT INTO `scan_record` VALUES (64, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\51f2af62-f27d-44e6-85b6-adf94dc794c8_20260611111759.jpg', '', '2026-06-11 11:18:01', '失败');
INSERT INTO `scan_record` VALUES (65, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\41b7cba5-b312-482d-9b0f-f1ad0c50b66b_20260611111802.jpg', '', '2026-06-11 11:18:04', '失败');
INSERT INTO `scan_record` VALUES (66, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\52f7074a-b3ce-44a4-a95c-58a7d1fe501a_20260611111804.jpg', '', '2026-06-11 11:18:06', '失败');
INSERT INTO `scan_record` VALUES (67, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\8b9d3440-6af0-455f-9d87-ac1c8f73a9c8_20260611111808.jpg', '', '2026-06-11 11:18:11', '失败');
INSERT INTO `scan_record` VALUES (68, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a9584528-d7a3-4f8f-99cb-7c0ec0ca6ace_20260611111811.jpg', '9787801791320', '2026-06-11 11:18:13', '成功');
INSERT INTO `scan_record` VALUES (69, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\7c035fb6-5b0a-430a-8480-ab1990502d3d_20260611142011.jpg', '', '2026-06-11 14:20:16', '失败');
INSERT INTO `scan_record` VALUES (70, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\70554ae1-a9ab-475b-8473-b45f02f05ee1_20260611142017.jpg', '9787801791320', '2026-06-11 14:20:20', '成功');
INSERT INTO `scan_record` VALUES (71, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\5b3b7ce9-00c6-41aa-9493-79ffa09d58f0_20260611142055.jpg', '9787801791320', '2026-06-11 14:20:57', '成功');
INSERT INTO `scan_record` VALUES (72, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\7c95c044-3d73-4da3-aa4a-2f69f201db27_20260611144743.jpg', '9787601191320', '2026-06-11 14:47:45', '成功');
INSERT INTO `scan_record` VALUES (73, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\b697d81a-a4d4-41ff-810e-66fbcd245b75_20260611144750.jpg', '9787801791320', '2026-06-11 14:47:53', '成功');
INSERT INTO `scan_record` VALUES (74, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\befa1fca-308c-4432-bde8-5b0c61749df7_20260611145109.jpg', '9787801791320', '2026-06-11 14:51:11', '成功');
INSERT INTO `scan_record` VALUES (75, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a4e36afc-e521-4b52-8a48-55f2726891da_20260611145720.jpg', '9787801791320', '2026-06-11 14:57:23', '成功');
INSERT INTO `scan_record` VALUES (76, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\581c5fbf-4422-4e4b-9016-3a3e59808663_20260611145804.jpg', '02', '2026-06-11 14:58:06', '成功');
INSERT INTO `scan_record` VALUES (77, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1818b97c-f6c9-43aa-bd4a-52a6a03f6e4f_20260611145808.jpg', '9787801791320', '2026-06-11 14:58:10', '成功');
INSERT INTO `scan_record` VALUES (78, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\b747621d-1b11-4497-b785-1d9f05c86a8e_20260611145838.jpg', '9787801791320', '2026-06-11 14:58:41', '成功');
INSERT INTO `scan_record` VALUES (79, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\e3b92287-5283-438a-ac9b-c91db0896e4a_20260612142634.jpg', '', '2026-06-12 14:26:36', '失败');
INSERT INTO `scan_record` VALUES (80, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\740fba87-95ea-4513-bd97-d89684522e30_20260612142640.jpg', '', '2026-06-12 14:26:41', '失败');
INSERT INTO `scan_record` VALUES (81, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\db561313-658f-46ac-9dbe-e8a87491de10_20260612142643.jpg', '', '2026-06-12 14:26:45', '失败');
INSERT INTO `scan_record` VALUES (82, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\5fb8cf02-7acd-4900-9e4f-57f29c418108_20260612142647.jpg', '', '2026-06-12 14:26:48', '失败');
INSERT INTO `scan_record` VALUES (83, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\65f7674b-ef7c-45e2-853b-06256aca20cf_20260612142650.jpg', '', '2026-06-12 14:26:51', '失败');
INSERT INTO `scan_record` VALUES (84, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\25c24943-00fb-4c33-bff1-9258d8b5e0aa_20260612142653.jpg', '', '2026-06-12 14:26:55', '失败');
INSERT INTO `scan_record` VALUES (85, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\c503c7bd-85f4-421b-a3ec-299c100478d0_20260614092036.jpg', '', '2026-06-14 09:20:37', '失败');
INSERT INTO `scan_record` VALUES (86, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\fd015410-93e2-45e6-9da5-3d88d6226ce3_20260614092039.jpg', '', '2026-06-14 09:20:40', '失败');
INSERT INTO `scan_record` VALUES (87, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\48dac108-378a-4c7e-b653-b92f4a1dbd4e_20260614092042.jpg', '', '2026-06-14 09:20:43', '失败');
INSERT INTO `scan_record` VALUES (88, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\8e424bd4-0c24-436e-b2da-cf132007c4dc_20260614092043.jpg', '', '2026-06-14 09:20:45', '失败');
INSERT INTO `scan_record` VALUES (89, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a8798fa4-9e28-456b-8a07-deb43ed2d3a4_20260614092046.jpg', '', '2026-06-14 09:20:47', '失败');
INSERT INTO `scan_record` VALUES (90, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\b7e3b958-69e5-430e-9acb-dff542c26279_20260614092055.jpg', '', '2026-06-14 09:20:57', '失败');
INSERT INTO `scan_record` VALUES (91, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\2b82ace0-4998-455a-a795-4df856c68a17_20260614092058.jpg', '', '2026-06-14 09:21:00', '失败');
INSERT INTO `scan_record` VALUES (92, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\fc7bcaab-ef7d-45e8-a55a-4c770aa1a818_20260614092102.jpg', '', '2026-06-14 09:21:03', '失败');
INSERT INTO `scan_record` VALUES (93, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\6e532775-6bb7-4b91-9eb9-4488b367a064_20260614092104.jpg', '', '2026-06-14 09:21:06', '失败');
INSERT INTO `scan_record` VALUES (94, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\cf9d71d2-e2c6-466f-b9b2-d87890d01b6c_20260614092109.jpg', '', '2026-06-14 09:21:10', '失败');
INSERT INTO `scan_record` VALUES (95, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\b168589b-ce67-4b24-97f6-51bc25545dae_20260614092113.jpg', '', '2026-06-14 09:21:14', '失败');
INSERT INTO `scan_record` VALUES (96, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\d9612db0-af0a-4468-ab8c-588ceecb99b7_20260614092119.jpg', '', '2026-06-14 09:21:21', '失败');
INSERT INTO `scan_record` VALUES (97, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\97f8f09c-7cad-41a4-b2a2-6f899ab7375a_20260614092122.jpg', '', '2026-06-14 09:21:23', '失败');
INSERT INTO `scan_record` VALUES (98, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\32c3ef47-0c1b-40a0-84e0-8984fefaca12_20260614092133.jpg', '', '2026-06-14 09:21:35', '失败');
INSERT INTO `scan_record` VALUES (99, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1d039510-fb96-42bd-834c-ad1f1227b0bd_20260614092136.jpg', '', '2026-06-14 09:21:38', '失败');
INSERT INTO `scan_record` VALUES (100, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a5d617cf-fa2c-4ac4-97c8-f02e260beab2_20260614092139.jpg', '', '2026-06-14 09:21:41', '失败');
INSERT INTO `scan_record` VALUES (101, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\7f8ab3bf-516c-470b-88fc-34982b38a1b3_20260614094136.jpg', '9787801791320', '2026-06-14 09:41:39', '成功');
INSERT INTO `scan_record` VALUES (102, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\163acbdf-c8a6-4ee7-b91c-1717b2b56b2a_20260614094210.jpg', '', '2026-06-14 09:42:13', '失败');
INSERT INTO `scan_record` VALUES (103, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\ba60f9c7-627d-4ede-837f-32a9fc69298b_20260614094216.jpg', '711881791320', '2026-06-14 09:42:19', '成功');
INSERT INTO `scan_record` VALUES (104, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a9a1b199-32e6-4d41-9c9d-eb18e6032c35_20260614094221.jpg', '02', '2026-06-14 09:42:24', '成功');
INSERT INTO `scan_record` VALUES (105, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\c6796a69-6c02-474d-95bf-a338629816be_20260614094226.jpg', '', '2026-06-14 09:42:30', '失败');
INSERT INTO `scan_record` VALUES (106, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\16e248c4-1461-488b-bc0c-80505debed06_20260614094230.jpg', '', '2026-06-14 09:42:34', '失败');
INSERT INTO `scan_record` VALUES (107, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\b3d588d6-6fcf-4ad6-b14a-6cb80086d1c8_20260614094240.jpg', '9787801791320', '2026-06-14 09:42:43', '成功');
INSERT INTO `scan_record` VALUES (108, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\bce7d863-5bc4-47ee-9279-78c94aeea4bd_20260614094257.jpg', '9787801791320', '2026-06-14 09:43:00', '成功');
INSERT INTO `scan_record` VALUES (109, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\da8ea982-912f-4c97-a1da-cb6ec274e0db_20260614094324.jpg', '', '2026-06-14 09:43:27', '失败');
INSERT INTO `scan_record` VALUES (110, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\36a69065-8f55-47fb-8ba9-60002ccf3a5c_20260614094329.jpg', '02', '2026-06-14 09:43:32', '成功');
INSERT INTO `scan_record` VALUES (111, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\0ea89f16-a33e-4f23-bf7c-ff5623f6e09b_20260614094333.jpg', '', '2026-06-14 09:43:36', '失败');
INSERT INTO `scan_record` VALUES (112, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\7081f198-a9cc-4302-a3f8-62de6ae4dfa2_20260614094337.jpg', '', '2026-06-14 09:43:40', '失败');
INSERT INTO `scan_record` VALUES (113, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\6e7aad27-679a-4194-9b9d-fde745525a3b_20260614094342.jpg', '', '2026-06-14 09:43:46', '失败');
INSERT INTO `scan_record` VALUES (114, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\86838eea-c1a2-4e9b-b5ed-8e3758c95ed5_20260614094348.jpg', '9787801791320', '2026-06-14 09:43:51', '成功');
INSERT INTO `scan_record` VALUES (115, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\08c2f44f-0570-480a-bbdb-889026cadae0_20260614094431.jpg', '', '2026-06-14 09:44:34', '失败');
INSERT INTO `scan_record` VALUES (116, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\63f4e7d8-c585-4380-9f6a-c4e75605cefb_20260614094440.jpg', '', '2026-06-14 09:44:43', '失败');
INSERT INTO `scan_record` VALUES (117, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\ad5985ea-da48-432c-8409-9dc96d4d0308_20260614094444.jpg', '9787801791320', '2026-06-14 09:44:47', '成功');
INSERT INTO `scan_record` VALUES (118, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\c410c377-ee5b-411f-bfd7-9fa45ab33746_20260614101252.jpg', '', '2026-06-14 10:12:55', '失败');
INSERT INTO `scan_record` VALUES (119, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a6f3b236-dd82-4548-8450-4f1785fb80c9_20260614101257.jpg', '9787801791320', '2026-06-14 10:12:59', '成功');
INSERT INTO `scan_record` VALUES (120, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\a0b22fe8-1b55-4314-a28b-2e0b9137611f_20260614102837.jpg', '9787801791320', '2026-06-14 10:28:39', '成功');
INSERT INTO `scan_record` VALUES (121, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\fad93175-a5a5-4e15-8250-5f6f124c7bc0_20260614102856.jpg', '9787801791320', '2026-06-14 10:28:58', '成功');
INSERT INTO `scan_record` VALUES (122, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\f4c6e7b1-a401-4348-b84a-49a9dc607842_20260614103224.jpg', '9787801791320', '2026-06-14 10:32:27', '成功');
INSERT INTO `scan_record` VALUES (123, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\0717313f-3eca-4047-a2d9-4e81f88c55d3_20260614103607.jpg', '', '2026-06-14 10:36:10', '失败');
INSERT INTO `scan_record` VALUES (124, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\71bb96a0-feb3-4a92-9b02-72483e7898b4_20260614103612.jpg', '9787801791320', '2026-06-14 10:36:14', '成功');
INSERT INTO `scan_record` VALUES (125, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\cca34149-d4be-4b1a-9728-386993e7a3d2_20260614103639.jpg', '', '2026-06-14 10:36:42', '失败');
INSERT INTO `scan_record` VALUES (126, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\00635212-d5e6-46a3-b7f1-5c0e26c7555e_20260614103644.jpg', '9787801791320', '2026-06-14 10:36:47', '成功');
INSERT INTO `scan_record` VALUES (127, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\894b1790-76c2-4511-8efa-81c32440e903_20260614103657.jpg', '', '2026-06-14 10:36:59', '失败');
INSERT INTO `scan_record` VALUES (128, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\3711403b-c8f3-4ec2-9f6f-349ede3378b7_20260614103700.jpg', '9787801791320', '2026-06-14 10:37:03', '成功');
INSERT INTO `scan_record` VALUES (129, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\1cfe4e9e-5bb3-4a54-91a3-a93880dfb136_20260614103703.jpg', '', '2026-06-14 10:37:05', '失败');
INSERT INTO `scan_record` VALUES (130, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\c533b331-652d-4d8b-b5ad-f36393c726aa_20260614103855.jpg', '9787801791320', '2026-06-14 10:38:57', '成功');
INSERT INTO `scan_record` VALUES (131, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\2c9893f5-4489-4e19-bab2-dbb04636de73_20260614103914.jpg', '', '2026-06-14 10:39:17', '失败');
INSERT INTO `scan_record` VALUES (132, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\292e8f5c-be3d-49c5-babb-47551387fcd7_20260614103919.jpg', '', '2026-06-14 10:39:21', '失败');
INSERT INTO `scan_record` VALUES (133, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\14b2ff91-a9eb-48a4-90bb-5d8c6c389f64_20260614103922.jpg', '14511264', '2026-06-14 10:39:24', '成功');
INSERT INTO `scan_record` VALUES (134, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\8c2e27ec-197b-4a37-8b53-f12f3d7c15fa_20260614103924.jpg', '9787801791320', '2026-06-14 10:39:27', '成功');
INSERT INTO `scan_record` VALUES (135, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\4164ebd0-6248-4cbb-87c6-257fe41ff4fb_20260614104612.jpg', '9787801791320', '2026-06-14 10:46:14', '成功');
INSERT INTO `scan_record` VALUES (136, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\db7cf7f2-de52-4a85-8f96-711678fe1c4b_20260614104640.jpg', '', '2026-06-14 10:46:43', '失败');
INSERT INTO `scan_record` VALUES (137, 'D:\\library_barcode_borrow\\backend\\library-borrow-backend\\upload\\fab2529c-c3d1-4360-8501-f7a489221693_20260614104645.jpg', '9787801791320', '2026-06-14 10:46:47', '成功');

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
  `user_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '123456' COMMENT '登录密码',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_account`(`user_account` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '师生与管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '张三', '2026001', 0, '2026-06-08 11:17:18', '123456');
INSERT INTO `sys_user` VALUES (3, '管理员', 'admin', 1, '2026-06-08 11:17:18', '123456');
INSERT INTO `sys_user` VALUES (4, '王五', '2006003', 0, '2026-06-14 09:40:44', '123456');

SET FOREIGN_KEY_CHECKS = 1;
