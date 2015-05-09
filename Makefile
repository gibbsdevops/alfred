compile:
	mvn compile assembly:single

all: doc compile

doc:
	mvn dependency:sources
	mvn dependency:resolve -Dclassifier=javadoc

db-server:
	if [ ! -d target/db ]; then initdb -A trust -N -D target/db; fi
	postgres -sFD target/db

db-reset:
	psql template1 -f scripts/sql/reset_db.sql

db-setup:
	psql dev -f scripts/sql/setup.sql

test-db:
	psql test -f scripts/sql/setup_test.sql

clean:
	mvn clean
