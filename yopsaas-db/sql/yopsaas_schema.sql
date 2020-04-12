drop database if exists yopsaas;
drop user 'yopsaas'@'127.0.0.1';
-- 支持emoji：需要mysql数据库参数： character_set_server=utf8mb4
create database yopsaas default character set utf8mb4 collate utf8mb4_unicode_ci;
use yopsaas;
create user 'yopsaas'@'127.0.0.1' identified by 'yopsaas123456';
grant all privileges on yopsaas.* to 'yopsaas'@'127.0.0.1';
flush privileges;