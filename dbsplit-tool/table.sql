drop table if exists test_table_$index;

create table test_table_$index
(
    id bigint not null,
    name varchar(128),
    ctime timestamp not null,
    primary key(id)         
);
