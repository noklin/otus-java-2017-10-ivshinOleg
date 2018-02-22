 create table if not exists DATA_SET (DATA_TYPE varchar(1) not null, id bigint not null auto_increment, number varchar(255), age integer, name varchar(255), street varchar(255), address_id bigint, primary key (id)) engine=MyISAM
 create table if not exists DATA_SET_DATA_SET (UserDataSet_id bigint not null, phones_id bigint not null, primary key (UserDataSet_id, phones_id)) engine=MyISAM
 