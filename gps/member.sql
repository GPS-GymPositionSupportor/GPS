create table gps.member
(
    user_id      bigint auto_increment
        primary key,
    m_id         varchar(255)           not null,
    m_password   varchar(255)           not null,
    name         varchar(255)           not null,
    nickname     varchar(255)           not null,
    email        varchar(255)           not null,
    birth        date                   not null,
    gender       varchar(255)           not null,
    authority    enum ('USER', 'ADMIN') not null,
    m_created_by varchar(255)           null,
    m_deleted_by varchar(255)           null,
    m_created_at timestamp              null,
    m_deleted_at timestamp              null,
    last_login   timestamp              null
);

