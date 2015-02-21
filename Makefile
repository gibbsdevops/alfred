compile:
	mvn compile assembly:single

all: doc compile

doc:
	mvn dependency:sources
	mvn dependency:resolve -Dclassifier=javadoc

clean:
	mvn clean
