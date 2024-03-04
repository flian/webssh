version='1.26'

#package project
cd ../
mvn clean package -DskipTests

#build local docker images
docker build -f scripts/docker/Dockerfile -t lotus/webssh:${version} .
docker tag lotus/webssh:${version} lotus/webssh:latest


#may need delete local tag and push again.
#docker rmi foylian/webssh:${version}
#docker rmi foylian/webssh:latest

#tag images with hub name
docker tag lotus/webssh:${version} foylian/webssh:${version}
docker tag lotus/webssh:${version} foylian/webssh:latest

#push images to docker hub
#docker push foylian/webssh:${version}
#docker push foylian/webssh:latest