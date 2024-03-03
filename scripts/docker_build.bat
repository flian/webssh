version='1.26'

#package project
cd ../
mvn clean package -DskipTests

#build local docker images
docker build -f scripts/docker/Dockerfile -t foylian/webssh:${version} .
docker tag foylian/webssh:${version} foylian/webssh:latest

#push images to docker hub
docker tag foylian/webssh:${version} foylian/webssh:${version}
docker tag foylian/webssh:${version} foylian/webssh:latest