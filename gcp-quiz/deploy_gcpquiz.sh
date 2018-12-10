#!/bin/bash

version=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec)
echo "Deploying $version"
component="kennieng.gcp-quiz"

docker stop $component
echo "Stopped existing $component"

docker rm $component

docker run -d --restart unless-stopped -p 8080:8080 -h "docker_`hostname`" --name $component kennieng/gcp-quiz:$version
echo "Deployed $component $version"
