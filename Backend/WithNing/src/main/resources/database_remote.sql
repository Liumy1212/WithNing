-- 数据库远程连接配置脚本
-- 执行此脚本以允许远程连接您的数据库

USE mysql;

-- 创建远程用户（如果需要）
-- 请将 'your_password' 替换为您的密码
CREATE USER IF NOT EXISTS 'remote_user'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON withning.* TO 'remote_user'@'%';
FLUSH PRIVILEGES;

-- 或者修改现有用户允许远程访问
UPDATE mysql.user SET Host='%' WHERE User='root' AND Host='localhost';
FLUSH PRIVILEGES;

-- 检查用户权限
SELECT User, Host FROM mysql.user;
