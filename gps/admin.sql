create table gps.admin
(
    admin_id   bigint auto_increment
        primary key,
    user_id    bigint    not null,
    last_login timestamp null,
    constraint FK_11_1
        foreign key (user_id) references gps.member (user_id)
);

create index FK_1
    on gps.admin (user_id);

