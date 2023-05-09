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

INSERT INTO self_database.oauth2_registered_client
    (id, authorization_grant_types, client_authentication_methods, client_id, client_id_issued_at, client_name, client_secret, client_secret_expires_at, client_settings, redirect_uris, scopes, token_settings) VALUES
('f7b6baa2-847b-4c17-8021-34c94d8108f9', 'refresh_token,client_credentials,authorization_code', 'client_secret_basic', 'messaging-client', '2023-05-09 19:11:30.926770000', '测试客户端配置', '$2a$10$U4t/3oQ3tL9MlvmTpjTe..uMNqBY6MiWcXyg8SuOenY.l3zq3OYXW', '2023-08-17 19:11:30.926770000', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}', 'http://127.0.0.1:8080/authorized,http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc', 'openid,profile,message.read,message.write', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000]}');
