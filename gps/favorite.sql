create table gps.favorite
(
    user_id     bigint                              not null,
    gym_id      bigint                              not null,
    added_at    timestamp default CURRENT_TIMESTAMP not null,
    is_favorite tinyint                             not null,
    primary key (user_id, gym_id),
    constraint FK_5
        foreign key (user_id) references gps.member (user_id),
    check (`is_favorite` in (0, 1))
);

create index FK_1
    on gps.favorite (user_id);

