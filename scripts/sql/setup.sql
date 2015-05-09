drop schema if exists alfred;
drop owned by alfred cascade;
drop user if exists alfred;

create user alfred;
create schema alfred;

alter schema alfred owner to alfred;
