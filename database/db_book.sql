/*
 Navicat Premium Dump SQL

 Source Server         : Mysqlfirst
 Source Server Type    : MySQL
 Source Server Version : 90200 (9.2.0)
 Source Host           : localhost:3306
 Source Schema         : db_book

 Target Server Type    : MySQL
 Target Server Version : 90200 (9.2.0)
 File Encoding         : 65001

 Date: 01/06/2026 13:08:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book_info
-- ----------------------------
DROP TABLE IF EXISTS `book_info`;
CREATE TABLE `book_info`  (
  `bookId` int NOT NULL AUTO_INCREMENT,
  `bookName` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `bookAuthor` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `bookPrice` decimal(10, 2) NOT NULL,
  `bookTypeId` int NOT NULL,
  `bookDesc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '书籍描述',
  `isBorrowed` tinyint NOT NULL COMMENT '1表示借出，0表示已还',
  `bookImg` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '书籍图片',
  PRIMARY KEY (`bookId`) USING BTREE,
  INDEX `fk_book_info_book_type_1`(`bookTypeId` ASC) USING BTREE,
  CONSTRAINT `book_info_ibfk_1` FOREIGN KEY (`bookTypeId`) REFERENCES `book_type` (`bookTypeId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 89 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book_info
-- ----------------------------
INSERT INTO `book_info` VALUES (14, '天龙八部', '金庸', 58.00, 6, '天龙八部乃金笔下的一部长篇小说，与《射雕》，《神雕》等 几部长篇小说一起被称为可读性最高的金庸小说。《天龙》的故事情节曲折，内容丰富，也曾多次被改编为电视作品。', 1, '/files/172956399543517291640144028.jpg');
INSERT INTO `book_info` VALUES (27, '明朝那些事儿', '当年明月', 399.00, 2, '国民史学读本，持续风行十余年，畅销3000万册，全本白话正说明朝大历史', 1, '/files/172956400090017291640208699.jpg');
INSERT INTO `book_info` VALUES (28, '沙丘', 'Frank Herbert', 394.90, 4, '每个\"不可不读\"的书单上都有《沙丘》！伟大的《沙丘》六部曲中文版初次完整出版！人类每次正视自己的渺小，都是自身的一次巨大进步。', 0, '/files/172956400629517291640430541.jpg');
INSERT INTO `book_info` VALUES (30, 'C Primer Plus', '史蒂芬·普拉达', 90.50, 1, 'C语言入门教程，C语言程序设计籍，程序员的启蒙教材，针对C11标准库更新', 0, '/files/172956401153917291640483012.jpg');
INSERT INTO `book_info` VALUES (31, '计算机网络：自顶向下方法', 'James，F.Kurose', 73.40, 1, '以自顶向下的方式系统展现计算机网络的原理与结构，广受欢迎的计算机网络教材。', 0, '/files/172956401698717291640550423.jpg');
INSERT INTO `book_info` VALUES (32, '围城', '钱钟书', 30.20, 3, '《围城》是一幅栩栩如生的世井百态图，人生的酸甜苦辣千般滋味均在其中得到了淋漓尽致的体现。钱钟书先生将自己的语言天才并入极其渊博的知识，再添加上一些讽刺主义的幽默调料，以一书而定江山。《围城》显示给我们一个真正的聪明人是怎样看人生，又怎样用所有作家都必得使用的文字来表述自己的\"观\"和\"感\"的。', 0, '/files/172956402393917291640653915.jpg');
INSERT INTO `book_info` VALUES (33, '平凡的世界', '路遥', 101.80, 3, '人生路遥，但没有白走的路；在平凡的世界里，照样可以活得丰富而精彩。《平凡的世界》激励了一代又一代青年人向上向善、自强不息，产生了广泛而深远的社会影响。读者从路遥身上获取励志的力量，正在于他的作品始终充盈着奋斗、激扬着拼搏，这是作品的魂魄，更是他人生的真实写照。', 0, '/files/172956402802917291640704876.jpg');
INSERT INTO `book_info` VALUES (34, '哈利波特', 'J.K.罗琳', 648.00, 6, '本书生动幽默，感人至深，而罗琳的创作经历就像这个故事本身一样令人印象深刻。与哈利·波特一样，J.K.罗琳的内心深藏着魔法。', 0, '/files/172956403420817291640819258.jpg');
INSERT INTO `book_info` VALUES (36, '新概念英语1', '亚历山大', 41.85, 7, '全新的教学理念、有趣的课文内容、全面的技能训练，提供完整、经过实践检验的英语学习体系！', 0, '/files/172956404306717291640980355.jpg');
INSERT INTO `book_info` VALUES (37, '新概念英语2', '亚历山大', 47.25, 7, '全新的教学理念、有趣的课文内容、全面的技能训练，提供完整、经过实践检验的英语学习体系！', 0, '/files/172956405109817291641036889.jpg');
INSERT INTO `book_info` VALUES (38, '新概念英语3', '亚历山大', 46.50, 7, '全新的教学理念、有趣的课文内容、全面的技能训练，提供完整、经过实践检验的英语学习体系！', 0, '/files/172956405866417291641148732.jpg');
INSERT INTO `book_info` VALUES (39, '新概念英语4', '亚历山大', 45.50, 7, '全新的教学理念、有趣的课文内容、全面的技能训练，提供完整、经过实践检验的英语学习体系！', 0, '/files/172956406475517291641216376.jpg');
INSERT INTO `book_info` VALUES (40, '数据结构', '严蔚敏', 39.80, 1, '计算机科学教材', 0, '/files/1729564073178172916402618610.jpg');
INSERT INTO `book_info` VALUES (41, '数据库系统概论', '王珊，萨师煊', 42.00, 1, '数据库经典教材', 0, '/files/1729564077935172916403170411.jpg');
INSERT INTO `book_info` VALUES (43, '献给阿尔吉侬的花束', '丹尼尔·凯斯', 36.00, 4, '声称能改造智能的科学实验在白老鼠阿尔吉侬身上获得了突破性的进展，下一步急需进行人体实验。个性和善、学习态度积极的心智障碍者查理·高登成为最佳人选。手术成功后，查理的智商从68跃升为185，然而那些从未有过的情绪和记忆也逐渐浮现。', 0, '/files/1729564082560172916403663712.jpg');
INSERT INTO `book_info` VALUES (44, '银河帝国1', 'Foundation', 36.50, 4, '人类蜗居在银河系的一个小角落——太阳系，在围绕太阳旋转的第三颗 行星上，生 活了十多万年之久。\n人类在这个小小的行星（他们称之为\"地球\"）上，建立了两百多个不同的行政区域（他们称之为\"国家\"），直到地球上诞生了第一个会思考的机器人。', 0, '/files/1729564086820172916408665610.jpg');
INSERT INTO `book_info` VALUES (45, '中国历代政治得失', '钱穆', 12.00, 2, '《中国历代政治得失》为作者的专题演讲合集，分别就中国汉、唐、宋、明、清五代的政府组织、百官职权、考试监察、财经赋税、兵役义务等种种政治制度作了提要勾玄的概观与比照，叙述因革演变，指陈利害得失。既高屋建瓴地总括了中国历史与政治的精要大义，又点明了近现代国人对传统文化和精神的种种误解。言简意赅，语重心长，实不失为一部简明的\"中国政治制度史\"。', 0, '/files/1729564097385172916410776711.jpg');
INSERT INTO `book_info` VALUES (46, '万历十五年', '黄仁宇', 18.00, 2, '万历十五年，亦即公元1587年，在西欧历史上为西班牙舰队全部出动征英的前一年；而在中国，这平平淡淡的一年中，发生了若干为历史学家所易于忽视的事件。这些事件，表面看来虽似末端小节，但实质上却是以前发生大事的症结，也是将在以后掀起波澜的机缘。在历史学家黄仁宇的眼中，其间的关系因果，恰为历史的重点，而我们的大历史之旅，也自此开始……', 0, '/files/1729564102030172916415913110.jpg');
INSERT INTO `book_info` VALUES (49, '追风筝的人', '卡勒德·胡赛尼', 29.00, 6, '12岁的阿富汗富家少爷阿米尔与仆人哈桑情同手足。然而，在一场风筝比赛后，发生了一件悲惨不堪的事，阿米尔为自己的懦弱感到自责和痛苦，逼走了哈桑，不久，自己也跟随父亲逃往美国。\n成年后的阿米尔始终无法原谅自己当年对哈桑的背叛。为了赎罪，阿米尔再度踏上暌违二十多年的故乡，希望能为不幸的好友尽最后一点心力，却发现一个惊天谎言，儿时的噩梦再度重演，阿米尔该如何抉择？', 0, '/files/1729564187277172916403663712.jpg');
INSERT INTO `book_info` VALUES (50, '白鹿原', '陈忠实', 39.00, 3, '在从清末民初到建国之初的半个世纪里，一阵阵狂风掠过了白鹿原上空，而每一次的变动都震荡着它的内在结构：打乱了再恢复，恢复了再打乱，细腻地反映出白姓和鹿姓两大家族祖孙三代的恩怨纷争。陈忠实先生在这里，人物的命运是纵线，百回千转，社会历史的演进是横面，愈拓愈宽，传统文化的兴衰则是全书的精神主体，以至人、社会历史、文化精神三者之间相互激荡，相互作用，共同推进了作品的时空，在我们眼前铺开了一轴恢宏的、动态的、极富纵深感的关于我们民族灵魂的现实主义的画卷。', 0, '/files/172956419212917291641148732.jpg');

-- ----------------------------
-- Table structure for book_type
-- ----------------------------
DROP TABLE IF EXISTS `book_type`;
CREATE TABLE `book_type`  (
  `bookTypeId` int NOT NULL AUTO_INCREMENT,
  `bookTypeName` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `bookTypeDesc` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '书籍类型描述',
  PRIMARY KEY (`bookTypeId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book_type
-- ----------------------------
INSERT INTO `book_type` VALUES (1, '计算机科学', '计算机相关');
INSERT INTO `book_type` VALUES (2, '历史', '历史相关');
INSERT INTO `book_type` VALUES (3, '文学', '文学相关');
INSERT INTO `book_type` VALUES (4, '科幻', '科幻相关');
INSERT INTO `book_type` VALUES (6, '小说', '小说相关');
INSERT INTO `book_type` VALUES (7, '外语', '外语相关');
INSERT INTO `book_type` VALUES (9, '测试类型', '测试类型');

-- ----------------------------
-- Table structure for borrow
-- ----------------------------
DROP TABLE IF EXISTS `borrow`;
CREATE TABLE `borrow`  (
  `borrowId` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `bookId` int NOT NULL,
  `borrowTime` datetime NOT NULL,
  `returnTime` datetime NULL DEFAULT NULL,
  `dueDate` datetime NULL DEFAULT NULL COMMENT '应还日期（借阅期限30天）',
  `isOverdue` tinyint NOT NULL DEFAULT 0 COMMENT '是否逾期，0否1是',
  `overdueDays` int NOT NULL DEFAULT 0 COMMENT '逾期天数',
  `fine` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '罚金（每天0.5元）',
  `renewCount` int NOT NULL DEFAULT 0 COMMENT '已续借次数（最多2次）',
  `lastRenewTime` datetime NULL DEFAULT NULL COMMENT '最后一次续借时间',
  PRIMARY KEY (`borrowId`) USING BTREE,
  INDEX `fk_borrow_user_1`(`userId` ASC) USING BTREE,
  INDEX `fk_borrow_book_info_1`(`bookId` ASC) USING BTREE,
  CONSTRAINT `borrow_ibfk_1` FOREIGN KEY (`bookId`) REFERENCES `book_info` (`bookId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `borrow_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of borrow
-- ----------------------------
INSERT INTO `borrow` VALUES (43, 15, 27, '2023-04-10 20:32:21', '2023-04-10 20:36:17', '2023-05-10 20:32:21', 0, 0, 0.00, 0, NULL);
INSERT INTO `borrow` VALUES (44, 15, 31, '2023-04-10 20:32:29', '2023-08-23 21:14:19', '2023-05-10 20:32:29', 1, 105, 52.50, 0, NULL);
INSERT INTO `borrow` VALUES (45, 15, 38, '2023-04-10 20:32:34', '2024-01-01 16:36:58', '2023-05-10 20:32:34', 1, 235, 117.50, 0, NULL);
INSERT INTO `borrow` VALUES (46, 15, 43, '2023-04-10 20:32:36', '2024-01-01 20:23:32', '2023-05-10 20:32:36', 1, 236, 118.00, 0, NULL);
INSERT INTO `borrow` VALUES (48, 16, 28, '2023-08-23 21:12:19', '2023-08-23 21:12:30', '2023-09-22 21:12:19', 0, 0, 0.00, 0, NULL);
INSERT INTO `borrow` VALUES (49, 15, 14, '2024-01-01 16:40:04', NULL, '2024-01-31 16:40:04', 0, 0, 0.00, 0, NULL);
INSERT INTO `borrow` VALUES (50, 15, 27, '2024-01-01 20:23:19', NULL, '2024-01-31 20:23:19', 0, 0, 0.00, 0, NULL);

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`  (
  `paymentId` int NOT NULL AUTO_INCREMENT,
  `borrowId` int NOT NULL,
  `userId` int NOT NULL,
  `amount` decimal(10, 2) NOT NULL COMMENT '缴费金额',
  `paymentTime` datetime NOT NULL COMMENT '缴费时间',
  `paymentType` tinyint NOT NULL COMMENT '1=罚金缴费',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`paymentId`) USING BTREE,
  INDEX `fk_payment_borrow`(`borrowId` ASC) USING BTREE,
  INDEX `fk_payment_user`(`userId` ASC) USING BTREE,
  CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`borrowId`) REFERENCES `borrow` (`borrowId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `payment_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of payment
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userId` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `userPassword` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `isAdmin` tinyint NOT NULL COMMENT '1是管理员，0非管理员',
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '123456', 1);
INSERT INTO `user` VALUES (2, '李明', '123456', 0);
INSERT INTO `user` VALUES (11, 'zhang', '123456', 0);
INSERT INTO `user` VALUES (13, 'zhao', '123456', 1);
INSERT INTO `user` VALUES (15, 'user', '123456', 0);
INSERT INTO `user` VALUES (16, 'user2', '123456', 0);
INSERT INTO `user` VALUES (17, 'user3', '123456', 0);
INSERT INTO `user` VALUES (18, 'admin2', '123456', 1);

SET FOREIGN_KEY_CHECKS = 1;
