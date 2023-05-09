-- 用户信息表
-- auto-generated definition
create table user_information_message
(
    id      int auto_increment
        primary key,
    email   varchar(64)  null,
    face_id varchar(128) null,
    id_card varchar(32)  null,
    sex     varchar(3)   null,
    constraint UK_ptse85ry7mp85a8wuyqls84i
        unique (id_card)
);

INSERT INTO user_information_message (id, email, face_id, id_card, sex) VALUES
(100, '18846439952@163.com', null, null, null);
