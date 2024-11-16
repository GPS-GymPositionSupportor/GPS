create table gps.gym
(
    gym_id        bigint auto_increment,
    g_name        varchar(255)                                not null,
    category      enum ('water', 'ball', 'physics', 'battle') not null,
    address1      varchar(255)                                not null,
    address2      varchar(255)                                not null,
    opening_hours varchar(255)                                null,
    homepage      varchar(255)                                null,
    phone_number  varchar(255)                                null,
    rating        double                                      null,
    g_longitude   double                                      not null,
    g_latitude    double                                      not null,
    g_created_by  varchar(255)                                null,
    g_deleted_by  varchar(255)                                null,
    g_created_at  timestamp                                   null,
    g_deleted_at  timestamp                                   null,
    primary key (gym_id, address1)
);

