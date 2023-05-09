-- 创建用户表
--      该表用于存储用户认证相关的数据
-- auto-generated definition
create table oauth2_application_user
(
    id        int auto_increment
        primary key,
    password  varchar(255) null,
    user_name varchar(255) null
);

insert into oauth2_application_user values
(100, '$2a$10$RcKN3i.XKY2T6A9SxLNe9ekPFMpsC7Je5OK9ad0dMVY2jpw5VB7TK', 'user1'), -- secret
(101, '$2a$10$RcKN3i.XKY2T6A9SxLNe9ekPFMpsC7Je5OK9ad0dMVY2jpw5VB7TK', 'user2')  -- secret