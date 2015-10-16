drop table if exists TEST_TABLE_$I;

create table TEST_TABLE_$I
(
    ID bigint not null,
    NAME varchar(128) not null,
    GENDER               smallint default 0, 
    LST_UPD_USER         varchar(128) default "SYSTEM",
    LST_UPD_TIME         timestamp default now(),
    primary key(id),
    unique key UK_NAME(NAME)
);
