create table gps.comment
(
    c_id      bigint auto_increment
        primary key,
    added_at  timestamp default CURRENT_TIMESTAMP not null,
    user_id   bigint                              not null,
    r_id      bigint                              not null,
    c_comment text                                not null,
    constraint FK_3
        foreign key (user_id) references gps.member (user_id),
    constraint FK_4
        foreign key (r_id) references gps.review (r_id)
);

create index FK_1
    on gps.comment (user_id);

create index FK_2
    on gps.comment (r_id);

