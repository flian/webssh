version='1.26'

#package project
cd ../
mvn clean package -DskipTests

#build local docker images
docker build -f scripts/docker/Dockerfile -t lotus/webssh:${version} .
docker tag lotus/webssh:${version} lotus/webssh:latest

#push images to docker hub
docker tag lotus/webssh:${version} foylian/webssh:${version}
docker tag lotus/webssh:${version} foylian/webssh:latest