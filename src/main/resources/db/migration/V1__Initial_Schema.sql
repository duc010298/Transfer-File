create extension if not exists "uuid-ossp";

create table if not exists app_user
(
    user_id            serial       not null primary key,
    user_name          varchar(36)  not null unique,
    encrypted_password varchar(128) not null
);

create table if not exists app_role
(
    role_id   bigint      not null primary key,
    role_name varchar(30) not null unique
);

create table if not exists user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint user_role_fk1 foreign key (user_id) references app_user (user_id),
    constraint user_role_fk2 foreign key (role_id) references app_role (role_id)
);

create table if not exists file
(
	file_id uuid primary key,
	user_id bigint not null,
	file_name varchar(255) not null,
	file_size bigint not null,
	date_upload timestamp not null,
	constraint file_fk1 foreign key (user_id) references app_user (user_id)
);