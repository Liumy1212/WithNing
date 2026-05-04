CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role TINYINT NOT NULL DEFAULT 2,
    points INT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `task` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    type TINYINT NOT NULL,
    reward_points INT NOT NULL,
    deadline DATETIME,
    image_url VARCHAR(255),
    publisher_id BIGINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `user_task` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    complete_time DATETIME,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `task_submit` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    task_id BIGINT,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    type TINYINT NOT NULL,
    reward_points INT NOT NULL,
    deadline DATETIME,
    image_url VARCHAR(255),
    status TINYINT NOT NULL DEFAULT 2,
    reviewer_id BIGINT,
    review_comment VARCHAR(200),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    review_time DATETIME
);

CREATE TABLE IF NOT EXISTS `point_record` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    points INT NOT NULL,
    description VARCHAR(200) NOT NULL,
    type TINYINT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `product` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    price INT NOT NULL,
    image_url VARCHAR(255),
    stock INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    publisher_id BIGINT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `exchange_apply` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    status TINYINT NOT NULL DEFAULT 2,
    reviewer_id BIGINT,
    review_comment VARCHAR(200),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    review_time DATETIME
);

CREATE TABLE IF NOT EXISTS `exchange_record` (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    exchange_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO user (username, password, nickname, email, phone, role, points) VALUES
('admin', '123456', '管理员', 'admin@example.com', '13800138000', 1, 1000),
('user1', '123456', '用户1', 'user1@example.com', '13800138001', 2, 100),
('user2', '123456', '用户2', 'user2@example.com', '13800138002', 2, 50);

INSERT INTO task (title, description, type, reward_points, deadline, image_url, publisher_id, status) VALUES
('每日打卡', '完成每日打卡任务', 1, 10, NULL, NULL, 1, 1),
('周打卡挑战', '完成一周的打卡任务', 2, 50, '2026-12-31 23:59:59', NULL, 1, 1),
('每日学习', '每天学习1小时', 1, 15, NULL, NULL, 1, 1);

INSERT INTO product (name, description, price, image_url, stock, status, publisher_id) VALUES
('积分兑换券', '可兑换100积分', 90, NULL, 100, 1, 1),
('精美笔记本', '高质量笔记本', 50, NULL, 50, 1, 1),
('咖啡券', '星巴克咖啡券', 30, NULL, 200, 2, 1);
