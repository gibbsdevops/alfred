compile:
	mvn compile assembly:single

all: doc compile

doc:
	mvn dependency:sources
	mvn dependency:resolve -Dclassifier=javadoc

upload:
	scp target/alfred-jar-with-dependencies.jar alfred.michelle.gibbsdevops.net:alfred
	ssh alfred.michelle.gibbsdevops.net sudo restart alfred

clean:
	mvn clean
