-- 创建数据库
CREATE DATABASE IF NOT EXISTS withning DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE withning;

-- 创建用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role TINYINT NOT NULL DEFAULT 2 COMMENT '1:管理员, 2:普通用户',
    points INT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建任务表
CREATE TABLE IF NOT EXISTS task (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    type TINYINT NOT NULL COMMENT '1:每日任务, 2:阶段任务',
    reward_points INT NOT NULL,
    deadline DATETIME,
    image_url VARCHAR(255),
    publisher_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1 COMMENT '1:已通过, 2:待审核, 3:已拒绝',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_publisher_id (publisher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建用户任务表
CREATE TABLE IF NOT EXISTS user_task (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    complete_time DATETIME,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_task_id (task_id),
    INDEX idx_completed (completed)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建任务申请表
CREATE TABLE IF NOT EXISTS task_submit (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    task_id BIGINT,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    type TINYINT NOT NULL,
    reward_points INT NOT NULL,
    deadline DATETIME,
    image_url VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 2 COMMENT '1:已通过, 2:待审核, 3:已拒绝',
    reviewer_id BIGINT,
    review_comment VARCHAR(200),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    review_time DATETIME,
    INDEX idx_user_id (user_id),
    INDEX idx_task_id (task_id),
    INDEX idx_status (status),
    INDEX idx_reviewer_id (reviewer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建积分记录表
CREATE TABLE IF NOT EXISTS point_record (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    points INT NOT NULL,
    description VARCHAR(200) NOT NULL,
    type TINYINT NOT NULL COMMENT '1:任务奖励, 2:惩罚, 3:兑换',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255),
    stock INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1 COMMENT '1:上架, 2:下架',
    publisher_id BIGINT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_price (price),
    INDEX idx_stock (stock),
    INDEX idx_status (status),
    INDEX idx_publisher_id (publisher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建兑换申请表
CREATE TABLE IF NOT EXISTS exchange_apply (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    status TINYINT NOT NULL DEFAULT 2 COMMENT '1:已通过, 2:待审核, 3:已拒绝',
    reviewer_id BIGINT,
    review_comment VARCHAR(200),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    review_time DATETIME,
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_status (status),
    INDEX idx_reviewer_id (reviewer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建兑换记录表
CREATE TABLE IF NOT EXISTS exchange_record (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    exchange_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_exchange_time (exchange_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入测试数据
-- 插入用户数据
INSERT INTO user (username, password, nickname, email, phone, role, points) VALUES
('admin', '123456', '管理员', 'admin@example.com', '13800138000', 1, 1000),
('user1', '123456', '用户1', 'user1@example.com', '13800138001', 2, 100),
('user2', '123456', '用户2', 'user2@example.com', '13800138002', 2, 50);

-- 插入任务数据
INSERT INTO task (title, description, type, reward_points, deadline, image_url, publisher_id, status) VALUES
('每日打卡', '完成每日打卡任务', 1, 10, NULL, NULL, 1, 1),
('周打卡挑战', '完成一周的打卡任务', 2, 50, '2026-12-31 23:59:59', NULL, 1, 1),
('每日学习', '每天学习1小时', 1, 15, NULL, NULL, 1, 1);

-- 插入商品数据
INSERT INTO product (name, description, price, image_url, stock, status, publisher_id) VALUES
('积分兑换券', '可兑换100积分', 90, NULL, 100, 1, 1),
('精美笔记本', '高质量笔记本', 50, NULL, 50, 1, 1),
('咖啡券', '星巴克咖啡券', 30, NULL, 200, 2, 1);
