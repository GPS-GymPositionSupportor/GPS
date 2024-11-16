create table gps.sns_like
(
    user_id    bigint                              not null
        primary key,
    gym_id     bigint                              not null,
    added_at   timestamp default CURRENT_TIMESTAMP not null,
    is_like    tinyint                             not null,
    like_count int                                 null,
    constraint FK_9
        foreign key (user_id) references gps.member (user_id),
    check (`is_like` in (0, 1))
);

create index FK_1
    on gps.sns_like (user_id);

