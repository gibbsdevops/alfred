create table alfred_user (
    id serial,
    login text not null,
    version int not null,
    github_id int not null,
    name text,
    email text,
    url text,
    html_url text,
    avatar_url text,
    type varchar(32),
    description text,
    github_created_at int,
    github_updated_at int,
    constraint alfred_user_pkey primary key (id)
);
create unique index alfred_user_unique_login on alfred_user (login);

create table alfred_git_user (
    id serial,
    name text,
    email text,
    constraint alfred_git_user_pkey primary key (id)
);
create unique index alfred_git_user_unique on alfred_git_user (name, email);

create table alfred_repo (
    id serial,
    version int not null,
    owner_id int,
    github_id int not null,
    name text,
    full_name text,
    private boolean,
    description text,
    fork boolean,
    url text,
    html_url text,
    ssh_url text,
    git_url text,
    clone_url text,
    github_created_at int,
    github_updated_at int,
    pushed_at int,
    homepage text,
    language text,
    default_branch text,
    constraint alfred_repo_pkey primary key (id)
);
create unique index alfred_repo_unique on alfred_repo (owner_id, name);

create table alfred_commit (
    id serial,
    version int not null,
    repo_id int not null,
    committer_id int not null,
    author_id int not null,
    pusher_id int not null,
    sender_id int not null,
    hash char(40) not null,
    message text,
    timestamp int not null,
    additions int,
    deletions int,
    constraint alfred_commit_pkey primary key (id)
);
create unique index alfred_commit_unique on alfred_commit (repo_id, hash);

create table alfred_job (
    id serial,
    version int not null,
    commit_id int not null,
    status text not null,
    error text,
    duration int,
    created_at int not null,
    constraint alfred_job_pkey primary key (id)
);

create table alfred_job_line (
    id serial,
    job_id int not null,
    index int not null,
    line text,
    constraint alfred_job_line_pkey primary key (id)
);
