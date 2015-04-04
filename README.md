# Alfred CI Server

### Requirements

* Java
* Nodejs + npm
* Maven

### Setup

```
$ npm install -g bower
$ bower install
$ mvn compile assembly:single
$ mvn exec:java -Dexec.mainClass="com.gibbsdevops.alfred.config.App"
```
