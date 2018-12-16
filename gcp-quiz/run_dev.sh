#!/bin/bash

mvn clean install -DskipDockerBuild

version=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)

java -jar target/gcp-quiz-$version.jar
