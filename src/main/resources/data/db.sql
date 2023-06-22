create database if not exists `urp`;

use urp;

create table if not exists file
(
    id          int unsigned auto_increment comment '业务主键'
        primary key,
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '最后更新时间',
    file_name   varchar(255) default ''                not null comment '文件名',
    user_id     int          default 0                 not null comment '用户ID',
    file_path   varchar(255) default ''                not null comment '文件路径',
    meet_id     int          default 0                 not null comment '组会ID',
    download    int          default 0                 not null comment '下载次数',
    deleted     tinyint      default 0                 not null comment '是否删除'
)
    comment 'file' collate = utf8mb4_unicode_ci;

create table if not exists meeting
(
    id          int unsigned auto_increment comment '业务主键'
        primary key,
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '最后更新时间',
    meet_name   varchar(255) default ''                not null comment '组会名称',
    begin_time  timestamp    default CURRENT_TIMESTAMP not null comment '开始时间',
    end_time    timestamp    default CURRENT_TIMESTAMP not null comment '结束时间',
    location    varchar(255) default ''                not null comment '会议地点',
    content     longtext                               not null comment '组会内容',
    subject     varchar(100) default ''                not null comment '会议主题',
    publisher   int          default 0                 not null comment '发表人ID',
    tag         int          default 0                 null comment '1-大一 2-大二 3-大三 4-大四 0-研究生',
    deleted     tinyint      default 0                 not null comment '是否删除'
)
    comment '组会记录表' collate = utf8mb4_unicode_ci;

create table if not exists project
(
    id           int unsigned auto_increment comment '业务主键'
        primary key,
    create_time  timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  timestamp default CURRENT_TIMESTAMP not null comment '最后更新时间',
    project_name varchar(255)                        not null comment '项目名称',
    head_id      int       default 0                 not null comment '负责人ID',
    deleted      tinyint   default 0                 not null comment '是否删除'
)
    collate = utf8mb4_unicode_ci;

create table if not exists user
(
    id          int unsigned auto_increment comment '业务主键'
        primary key,
    create_time timestamp    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp    default CURRENT_TIMESTAMP not null comment '最后更新时间',
    user_name   varchar(50)  default ''                not null comment '账号',
    pass_word   varchar(128) default ''                not null comment '密码',
    deleted     tinyint      default 0                 not null comment '是否删除'
)
    comment 'user' collate = utf8mb4_unicode_ci;

create table if not exists user_info
(
    id             int unsigned auto_increment comment '业务主键'
        primary key,
    create_time    timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    timestamp default CURRENT_TIMESTAMP not null comment '最后更新时间',
    user_id        int       default 0                 not null comment '用户ID',
    user_info_name varchar(255)                        not null comment '姓名',
    student_id     varchar(255)                        not null comment '学号',
    user_role      int       default 0                 not null comment '0-普通用户 1-组长 2-老师',
    grade          varchar(255)                        not null comment '年级',
    deleted        tinyint   default 0                 not null comment '是否删除'
)
    comment 'user_info' collate = utf8mb4_unicode_ci;


insert INTO user (id ,user_name, pass_word) value (1, 'admin', '8163863c634ceb460f1350d127cf121a');
insert into user_info (user_id, user_info_name, student_id, grade, user_role)
values (1, '谭老师', '', '', 2);