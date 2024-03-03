version='1.26'
SHELL_FOLDER=$(cd "$(dirname "$0")";pwd)
cd ${SHELL_FOLDER}

#编译项目
cd ../
mvn clean package -DskipTests

#编译
docker build -f scripts/docker/Dockerfile -t lotus/webssh:${version} .
docker tag lotus/webssh:${version} lotus/webssh:latest

#中央仓库
docker tag lotus/webssh:${version} foylian/webssh:${version}
docker tag lotus/webssh:${version} foylian/webssh:latest