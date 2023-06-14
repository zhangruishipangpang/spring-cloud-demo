-- Client 信息
--      该表用于配置接入中心认证平台的客户平台
-- auto-generated definition
create table oauth2_registered_client
(
    id                            varchar(255)  not null
        primary key,
    authorization_grant_types     varchar(255)  null,
    client_authentication_methods varchar(255)  null,
    client_id                     varchar(255)  null,
    client_id_issued_at           datetime(6)   null,
    client_name                   varchar(255)  null,
    client_secret                 varchar(255)  null,
    client_secret_expires_at      datetime(6)   null,
    client_settings               varchar(1000) null,
    redirect_uris                 varchar(255)  null,
    scopes                        varchar(255)  null,
    token_settings                varchar(1000) null,
    constraint oauth2_registered_client_client_id_client_secret_uindex
        unique (client_id, client_secret)
);

-- 统一授权页临时token
CREATE TABLE oauth2_authorization_consent (
      registered_client_id varchar(100) NOT NULL,
      principal_name varchar(200) NOT NULL,
      authorities varchar(1000) NOT NULL,
      PRIMARY KEY (registered_client_id, principal_name)
);

/*
IMPORTANT:
    If using PostgreSQL, update ALL columns defined with 'blob' to 'text',
    as PostgreSQL does not support the 'blob' data type.
*/
CREATE TABLE oauth2_authorization (
      id varchar(100) NOT NULL,
      registered_client_id varchar(100) NOT NULL,
      principal_name varchar(200) NOT NULL,
      authorization_grant_type varchar(100) NOT NULL,
      authorized_scopes varchar(1000) DEFAULT NULL,
      attributes blob DEFAULT NULL,
      state varchar(500) DEFAULT NULL,
      authorization_code_value blob DEFAULT NULL,
      authorization_code_issued_at timestamp DEFAULT NULL,
      authorization_code_expires_at timestamp DEFAULT NULL,
      authorization_code_metadata blob DEFAULT NULL,
      access_token_value blob DEFAULT NULL,
      access_token_issued_at timestamp DEFAULT NULL,
      access_token_expires_at timestamp DEFAULT NULL,
      access_token_metadata blob DEFAULT NULL,
      access_token_type varchar(100) DEFAULT NULL,
      access_token_scopes varchar(1000) DEFAULT NULL,
      oidc_id_token_value blob DEFAULT NULL,
      oidc_id_token_issued_at timestamp DEFAULT NULL,
      oidc_id_token_expires_at timestamp DEFAULT NULL,
      oidc_id_token_metadata blob DEFAULT NULL,
      refresh_token_value blob DEFAULT NULL,
      refresh_token_issued_at timestamp DEFAULT NULL,
      refresh_token_expires_at timestamp DEFAULT NULL,
      refresh_token_metadata blob DEFAULT NULL,
      PRIMARY KEY (id)
);

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
