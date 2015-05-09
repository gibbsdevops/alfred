drop schema if exists alfred_test;
drop owned by alfred_test cascade;
drop user if exists alfred_test;

create user alfred_test;
create schema alfred_test;

alter schema alfred_test owner to alfred_test;
