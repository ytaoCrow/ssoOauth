CREATE TABLE oauth_client_details(
    client_id varchar(256) primary key comment '用於標示客戶端，類似AppKey',
    resource_ids varchar(256) comment 'client 端 存取的資源 ID 集合，以 dot 分隔。例如order-resource,pay-resource',
    client_secret varchar(256) comment 'client 端存取金鑰',
    scope varchar(256) comment 'client 端存取權限範圍',
    authorized_grant_types varchar(256) comment '可選值，如授權碼模式:authorization_code；密碼模式:password；刷新token:refresh_token；隱藏模式:implicit；客户端模式:client_credentials，支援多種方式以逗號分隔，例如：password,refresh_token',
    web_server_redirect_uri varchar(256) comment '客户端重定向URL，authorization_code和implicit模式時需要進行檢核，例如:http://google.com',
    authorities varchar(256) comment '可null，指定使用者權限範圍，如果授權過程需要使用者登入，該字段不生效，implicit和client_credentials模式時需要；例如：ROLE_ADMIN,ROLE_USER',
    access_token_validity integer comment '可null，設定access_token的有效時間(秒)，默認12小時',
    refresh_token_validity integer comment '可null，設定refresh_token有效期限(秒），默認30天',
    additional_information varchar(4096) comment '默認false,適用authorization_code模式,設置使用者是否自動approval操作,設置true跳過用戶確認授權操作頁面，直接跳到redirect_uri',
    autoapprove varchar(256)
);

CREATE TABLE oauth_client_token(
    token_id varchar(256) comment '從serve端獲得的access_token的值',
    token blob comment '這是一個二進制的字元, 儲存的資料是OAuth2AccessToken.java序列化後的二進制的資料',
    authentication_id varchar(256) primary key comment '根據當前的username(如果有),client_id與scope通過MD5加密生成的',
    user_name varchar(256),
    client_id varchar(256)
);

create table oauth_access_token (
    token_id varchar(256) comment '该字段的值是将access_token的值通过MD5加密后存储的',
    token blob comment '存储将OAuth2AccessToken.java对象序列化后的二进制数据, 是真实的AccessToken的数据值',
    authentication_id varchar(256) primary key comment '该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的(参考->DefaultClientKeyGenerator.java类)',
    user_name varchar(256) comment '登录时的用户名,若客户端没有用户名(如grant_type="client_credentials"),则该值等于client_id',
    client_id varchar(256) comment '客户端ID',
    authentication blob comment '存储将OAuth2Authentication.java对象序列化后的二进制数据',
    refresh_token varchar(256) comment '该字段的值是将refresh_token的值通过MD5加密后存储的'
);

create table oauth_refresh_token (
    token_id varchar(256) comment '该字段的值是将refresh_token的值通过MD5加密后存储的',
    token blob comment '存储将OAuth2RefreshToken.java对象序列化后的二进制数据',
    authentication binary comment '存储将OAuth2Authentication.java对象序列化后的二进制数据'
);

create table oauth_code (
    code varchar(256) comment '存储服务端系统生成的code的值(未加密)',
    authentication blob comment '存储将AuthorizationRequestHolder.java对象序列化后的二进制数据'
);

create table oauth_approvals (
    userId varchar(256) comment '授权用户ID',
    clientId varchar(256) comment '客户端ID',
    scope varchar(256) comment '授权访问',
    status varchar(10) comment '授权状态，例如：APPROVED',
    expiresAt timestamp comment '授权失效时间',
    lastModifiedAt timestamp default current_timestamp comment '最后修改时间'
);

#生成供资源服务器使用的受信任Client配置
insert into `oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) values ('resourceclient', null, '{noop}123456', 'all,read,write', 'authorization_code,refresh_token,password', 'http://www.baidu.com', 'role_trusted_client', 7200, 7200, null, 'true');

#生成接入方测试Client配置信息
insert intO `oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('accessDemo', NULL, '{noop}123456', 'all,read,write', 'authorization_code,refresh_token,password', 'http://www.baidu.com', 'ROLE_TRUSTED_CLIENT', 7200, 7200, NULL, 'true');