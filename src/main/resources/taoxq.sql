create database taoxq;

drop table if exists audit;
drop table if exists notification;
drop table if exists user_comment_like;
drop table if exists user_discuss_like;
drop table if exists user_interest;
drop table if exists user_user_star;
drop table if exists user_discuss_star;
drop table if exists discuss_interest;
drop table if exists interest;
drop table if exists reply;
drop table if exists comment;
drop table if exists discuss;
drop table if exists user;

create table user
(
    id           int          not null auto_increment,
    root         integer      not null default 0,
    username     varchar(50)  not null,
    password     varchar(100) not null,
    email        varchar(30)  not null,
    gender       varchar(1),
    hometown     varchar(50),
    phone_number varchar(15),
    intro        varchar(15),
    is_waiting   boolean      not null default false,
    state        integer      not null default 0,
    time         datetime     not null default current_timestamp,
    primary key (id)
);

create table audit
(
    id        int      not null auto_increment,
    sender_id int      not null,
    user_id   int      not null,
    state     int      not null,
    content   varchar(30),
    done      boolean  not null default false,
    time      datetime not null default current_timestamp,
    primary key (id)
);

create table notification
(
    id        int      not null auto_increment,
    sender_id int      not null,
    getter_id int      not null,
    content   varchar(100),
    is_read   boolean  not null default false,
    time      datetime not null default current_timestamp,
    primary key (id)
);

create table discuss
(
    id          int      not null auto_increment,
    user_id     int      not null,
    title       varchar(100),
    like_num    int      not null default 0,
    dislike_num int      not null default 0,
    star_num    int      not null default 0,
    comment_num int      not null default 0,
    time        datetime not null default current_timestamp,
    primary key (id)
);

create table comment
(
    id          int      not null auto_increment,
    discuss_id  int      not null,
    user_id     int      not null,
    like_num    int      not null default 0,
    dislike_num int      not null default 0,
    reply_num   int      not null default 0,
    time        datetime not null default current_timestamp,
    primary key (id)
);

create table reply
(
    id          int      not null auto_increment,
    comment_id  int      not null,
    user_id     int      not null,
    to_reply_id int      not null,
    time        datetime not null default current_timestamp,
    is_read     boolean  not null default false,
    primary key (id)
);

create table interest
(
    id        int         not null auto_increment,
    name      varchar(20) not null,
    parent_id int         not null,
    primary key (id)
);

create table discuss_interest
(
    id          int not null auto_increment,
    interest_id int not null,
    discuss_id  int not null,
    primary key (id)
);

create table user_discuss_star
(
    id         int      not null auto_increment,
    user_id    int      not null,
    discuss_id int      not null,
    time       datetime not null default current_timestamp,
    is_read    boolean  not null default false,
    primary key (id)
);

create table user_user_star
(
    id           int      not null auto_increment,
    user_id      int      not null,
    star_user_id int      not null,
    time         datetime not null default current_timestamp,
    is_read      boolean  not null default false,
    primary key (id)
);

create table user_interest
(
    id          int not null auto_increment,
    interest_id int not null,
    user_id     int not null,
    primary key (id)
);

create table user_discuss_like
(
    id         int      not null auto_increment,
    user_id    int      not null,
    discuss_id int      not null,
    time       datetime not null default current_timestamp,
    is_like    bool     not null,
    is_read    boolean  not null default false,
    primary key (id)
);

create table user_comment_like
(
    id         int      not null auto_increment,
    user_id    int      not null,
    comment_id int      not null,
    time       datetime not null default current_timestamp,
    is_like    bool not null,
    is_read    boolean  not null default false,
    primary key (id)
);

