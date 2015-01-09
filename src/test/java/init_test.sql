create database test_rdb;

grant all privileges on test_rdb.* to 'test_rdb'@localhost identified by 'test_rdb';

use test_rdb;

create table User (
    id bigint not null,
    email varchar(100) not null,
    passwd varchar(100) not null,
    name varchar(100) not null,
    gender boolean not null,
    aboutMe varchar(100) null,
    birth date not null,
    lastLoginAt datetime null,
    createdAt timestamp not null,
    updatedAt timestamp not null,
    version bigint not null,
    primary key (id)
);

create table Book (
    id bigint not null,
    author_id bigint not null,
    name varchar(100) not null,
    description text null,
    publishAt date not null,
    createdAt timestamp not null,
    updatedAt timestamp not null,
    version bigint not null,
    primary key (id)
);
