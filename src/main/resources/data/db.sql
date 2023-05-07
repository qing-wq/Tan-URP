create database if not exists `urp`;

use urp;

DROP TABLE IF EXISTS meeting;
CREATE TABLE meeting
(
    `id`          INT(10)      NOT NULL AUTO_INCREMENT COMMENT '业务主键',
    `create_time` timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
    `update_time` timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '最后更新时间',
    `meet_name`   VARCHAR(255) NOT NULL COMMENT '组会名称',
    `begin_time`  timestamp    NOT NULL COMMENT '开始时间',
    `end_time`    timestamp    NOT NULL COMMENT '结束时间',
    `location`    VARCHAR(255) NOT NULL COMMENT '会议地点',
    `content`     VARCHAR(255) NOT NULL COMMENT '组会内容',
    `tag`         INT(10)               DEFAULT 0 COMMENT '1-大一 2-大二 3-大三 4-大四 0-研究生',
    `deleted`     tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (id)
) COMMENT = '组会记录表';

DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    `id`          INT(10)      NOT NULL AUTO_INCREMENT COMMENT '业务主键',
    `create_time` timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
    `update_time` timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '最后更新时间',
    `user_name`   VARCHAR(255) NOT NULL COMMENT '账号',
    `pass_word`   VARCHAR(255) NOT NULL COMMENT '密码',
    `deleted`     tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (id)
) COMMENT = 'user';
DROP TABLE IF EXISTS file;
CREATE TABLE file
(
    `id`          INT(10)      NOT NULL AUTO_INCREMENT COMMENT '业务主键',
    `create_time` timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
    `update_time` timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '最后更新时间',
    `file_name`   VARCHAR(255) NOT NULL COMMENT '文件名',
    `user_id`     VARCHAR(255) NOT NULL COMMENT '用户ID',
    `file_path`   VARCHAR(255) NOT NULL COMMENT '文件路径',
    `meet_id`     VARCHAR(255) NOT NULL COMMENT '组会ID',
    `deleted`     tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (id)
) COMMENT = 'file';
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info
(
    `id`             INT(10)      NOT NULL AUTO_INCREMENT COMMENT '业务主键',
    `create_time`    timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
    `update_time`    timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '最后更新时间',
    `user_id`        VARCHAR(255) NOT NULL COMMENT '用户ID',
    `user_info_name` VARCHAR(255) NOT NULL COMMENT '姓名',
    `student_id`     VARCHAR(255) NOT NULL COMMENT '学号',
    `user_role`      VARCHAR(255) NOT NULL COMMENT '用户角色',
    `grade`          VARCHAR(255) NOT NULL COMMENT '年级',
    `deleted`        tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (id)
) COMMENT = 'user_info';
DROP TABLE IF EXISTS project;
CREATE TABLE project
(
    `id`           INT(10)      NOT NULL AUTO_INCREMENT COMMENT '业务主键',
    `create_time`  timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '创建时间',
    `update_time`  timestamp    NOT NULL DEFAULT current_timestamp() COMMENT '最后更新时间',
    `project_name` VARCHAR(255) NOT NULL COMMENT '项目名称',
    `head_id`      VARCHAR(255) NOT NULL COMMENT '负责人ID',
    `deleted`      tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (id)
) COMMENT = '';

