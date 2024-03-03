#just a place hoder,prepare for next release
#frolvlad/alpine-oraclejre8:slim
#pivotalservices/jdk8-minimal:latest
#https://www.cnblogs.com/lshan/p/10113284.html

FROM frolvlad/alpine-oraclejre8:slim

MAINTAINER foylian <ljs2342003@gmail.com>

ARG JAR_FULL_NAME=vue2-sshserver-1.26.jar
ARG WORKDIR_PATH=/app/webssh
ARG JAR_FULL_PATH=$WORKDIR_PATH/$JAR_FULL_NAME
WORKDIR $WORKDIR_PATH

COPY vue2-sshserver/target/$JAR_FULL_NAME $JAR_FULL_PATH

EXPOSE 5443 5132

ENTRYPOINT ["sh", "-c","java $JAVA_OPTS -jar $JAR_FULL_PATH $SPRING_BOOT_OPTS"]
