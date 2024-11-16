create table gps.review
(
    r_id      bigint auto_increment
        primary key,
    added_at  timestamp default CURRENT_TIMESTAMP not null,
    user_id   bigint                              not null,
    gym_id    bigint                              not null,
    r_comment text                                not null,
    constraint FK_7
        foreign key (user_id) references gps.member (user_id)
);

create index FK_1
    on gps.review (user_id);

