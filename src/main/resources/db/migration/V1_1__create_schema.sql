create table users
(
    id         bigserial     not null primary key,
    name       varchar(100)  not null,
    email      varchar(255)  not null unique,
    password   varchar(500)  not null,
    gender     varchar(10)   not null,
    age        int           not null,
    weight     numeric(4, 1) not null,
    growth     numeric(5, 2) not null,
    daily_norm int           not null,
    goal       varchar(50)   not null,
    activity   varchar(50)   not null,
    role       varchar(50)   not null default 'ROLE_USER'
);

create table dishes
(
    id       bigserial    not null primary key,
    name     varchar(100) not null,
    cals     int          not null,
    proteins int          not null,
    fats     int          not null,
    carbs    int          not null
);

create table meals
(
    id      bigserial not null primary key,
    name    varchar(100) default 'Прием пищи',
    date    date      not null,
    user_id bigint    not null references users (id)
);

create table portions
(
    id      bigserial not null primary key,
    dish_id bigint    not null references dishes (id),
    grams   int       not null
);

create table meals_portions(
    meal_id bigint not null references meals(id),
    portion_id bigint not null references portions(id),
    primary key (meal_id, portion_id)
);