create table presentation
(
    id          bigint not null auto_increment
        primary key,
    name        varchar(255),
    description varchar(255),
    tenant_id   varchar(255),
    created_at  datetime
);