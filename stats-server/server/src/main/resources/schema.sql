drop table if exists endpoint_hit;

create table if not exists
endpoint_hit (
id integer PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY NOT NULL,
app	varchar(100),
uri	varchar(2000),
ip varchar(20),
timestamp timestamp
);