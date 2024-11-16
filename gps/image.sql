create table gps.image
(
    id       bigint auto_increment
        primary key,
    caption  varchar(255) not null,
    added_at datetime     not null,
    ImageUrl varchar(255) not null,
    user_id  bigint       null,
    gym_id   bigint       null,
    r_id     bigint       null,
    constraint FK_11
        foreign key (user_id) references gps.member (user_id),
    constraint FK_12_1
        foreign key (gym_id) references gps.gym (gym_id),
    constraint FK_13
        foreign key (r_id) references gps.review (r_id)
);

create index FK_1
    on gps.image (user_id);

create index FK_2
    on gps.image (gym_id);

create index FK_3
    on gps.image (r_id);

