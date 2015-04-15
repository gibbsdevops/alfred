create table alfred_user (
    id int not null,
    login varchar(256),
    name varchar(256),
    email varchar(256),
    url varchar(256),
    html_url varchar(256),
    avatar_url varchar(256),
    type varchar(256),
    description varchar(256),
    created_at int,
    updated_at int,
    constraint alfred_user_pkey primary key (id)
);

create table alfred_git_user (
    id serial,
    name varchar(256),
    email varchar(256),
    constraint alfred_git_user_pkey primary key (id)
);

create unique index alfred_git_user_unique on alfred_git_user (name, email);
