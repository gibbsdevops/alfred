create table alfred_user (
    id serial,
    login varchar(256) not null,
    version int not null,
    github_id int not null,
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
create unique index alfred_user_unique_login on alfred_user (login);

create table alfred_git_user (
    id serial,
    name varchar(256),
    email varchar(256),
    constraint alfred_git_user_pkey primary key (id)
);
create unique index alfred_git_user_unique on alfred_git_user (name, email);

create table alfred_repo (
    id int not null,
    name varchar(256),
    full_name varchar(256),
    private bit,
    description varchar(256),
    fork bit,
    url varchar(256),
    html_url varchar(256),
    ssh_url varchar(256),
    git_url varchar(256),
    clone_url varchar(256),
    created_at int,
    updated_at int,
    pushed_at int,
    homepage varchar(256),
    language varchar(256),
    default_branch varchar(256),
    constraint alfred_repo_pkey primary key (id)
);
