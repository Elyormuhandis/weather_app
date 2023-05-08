create table if not exists city
(
    id                 serial primary key,
    version            int                not null default 1,
    name               varchar(64) unique not null,
    enabled            boolean            not null default true,
    created_date       timestamp          not null default current_timestamp,
    last_modified_date timestamp          not null default current_timestamp
);

create table if not exists person
(
    id                 serial primary key,
    version            int                not null default 1,
    full_name          varchar(100)       not null,
    username           varchar(64) unique not null,
    password           varchar(100)       not null,
    role               varchar(100)       not null,
    created_date       timestamp          not null default current_timestamp,
    last_modified_date timestamp          not null default current_timestamp
);

create table if not exists weather
(
    id                 serial primary key,
    version            int          not null default 1,
    description        varchar(100) not null,
    temperature        int          not null,
    created_date       timestamp    not null default current_timestamp,
    last_modified_date timestamp    not null default current_timestamp
);

alter table city
    add column weather_id int not null constraint city_weather_fk_city references weather (id)
on update cascade on delete cascade;


create table if not exists city_person
(
    id        serial primary key,
    city_id   int not null,
    person_id int not null,
    FOREIGN KEY (city_id) REFERENCES city(id),
    FOREIGN KEY (person_id) REFERENCES person(id),
    UNIQUE (city_id, person_id)
);
