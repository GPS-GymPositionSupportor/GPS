create table gps.real_time_location
(
    user_id   bigint    not null
        primary key,
    longitude double    not null,
    latitude  double    not null,
    time      timestamp not null
);

