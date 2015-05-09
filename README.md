# Alfred CI Server

### Requirements

* Java
* Nodejs + npm
* Maven

#### Setup

```
$ npm install -g bower
$ bower install
$ mvn compile assembly:single
$ mvn exec:java -Dexec.mainClass="com.gibbsdevops.alfred.config.App"
```

### Database

```
# requires postgres in PATH
make db-server

# creates dev database
make db-reset

# creates users and schemas
make db-setup
```

#### Useful PostgreSQL Commands

* `\dn`, list all schemas
* `\dt alfred.*` list all tables in schema
* `\x on` turn on extended display format
