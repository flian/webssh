version='1.26'
SHELL_FOLDER=$(cd "$(dirname "$0")";pwd)
cd ${SHELL_FOLDER}

#编译项目
cd ../
mvn clean package -DskipTests

#编译
docker build -f scripts/docker/Dockerfile -t foylian/webssh:${version} .
docker tag foylian/webssh:${version} foylian/webssh:latest

#中央仓库
docker tag foylian/webssh:${version} foylian/webssh:${version}
docker tag foylian/webssh:${version} foylian/webssh:latest