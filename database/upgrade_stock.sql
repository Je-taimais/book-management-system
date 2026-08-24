-- ============================================
-- 图书库存字段升级脚本
-- 用途：为已有 book_info 表增加 stock 字段
-- 执行方式：在 MySQL 中执行此脚本即可
-- 安全性：仅 ALTER TABLE + UPDATE，不影响已有数据
-- 日期：2026-06-01
-- ============================================

-- 1. 增加 stock 字段（如果不存在则添加）
ALTER TABLE `book_info`
  ADD COLUMN IF NOT EXISTS `stock` int NOT NULL DEFAULT 3 COMMENT '库存数量，可借数量'
  AFTER `isBorrowed`;

-- 2. 为现有图书随机设置 3~5 本库存
UPDATE `book_info` SET `stock` = 5 WHERE `bookId` = 14;
UPDATE `book_info` SET `stock` = 3 WHERE `bookId` = 27;
UPDATE `book_info` SET `stock` = 3 WHERE `bookId` = 28;
UPDATE `book_info` SET `stock` = 5 WHERE `bookId` = 30;
UPDATE `book_info` SET `stock` = 4 WHERE `bookId` = 31;
UPDATE `book_info` SET `stock` = 4 WHERE `bookId` = 32;
UPDATE `book_info` SET `stock` = 5 WHERE `bookId` = 33;
UPDATE `book_info` SET `stock` = 3 WHERE `bookId` = 34;
UPDATE `book_info` SET `stock` = 4 WHERE `bookId` = 36;
UPDATE `book_info` SET `stock` = 3 WHERE `bookId` = 37;
UPDATE `book_info` SET `stock` = 3 WHERE `bookId` = 38;
UPDATE `book_info` SET `stock` = 5 WHERE `bookId` = 39;
UPDATE `book_info` SET `stock` = 4 WHERE `bookId` = 40;
UPDATE `book_info` SET `stock` = 3 WHERE `bookId` = 41;
UPDATE `book_info` SET `stock` = 4 WHERE `bookId` = 43;
UPDATE `book_info` SET `stock` = 5 WHERE `bookId` = 44;
UPDATE `book_info` SET `stock` = 3 WHERE `bookId` = 45;
UPDATE `book_info` SET `stock` = 4 WHERE `bookId` = 46;
UPDATE `book_info` SET `stock` = 5 WHERE `bookId` = 49;
UPDATE `book_info` SET `stock` = 4 WHERE `bookId` = 50;
