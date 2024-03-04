#just a place hoder,prepare for next release
# avaliable jdk base:(will chose one as base)
#frolvlad/alpine-oraclejre8:slim
#pivotalservices/jdk8-minimal:latest
#openjdk:8-jdk-alpine
#https://www.cnblogs.com/lshan/p/10113284.html
#push image to hub or replace tag https://blog.51cto.com/u_16175439/7044317
FROM openjdk:8-jdk-alpine


MAINTAINER foylian <ljs2342003@gmail.com>
WORKDIR $WORKDIR_PATH

# set timezone
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ARG JAR_FULL_NAME=vue2-sshserver-1.26.jar
ARG WORKDIR_PATH=/app/webssh
ARG JAR_FULL_PATH=$WORKDIR_PATH/$JAR_FULL_NAME


COPY vue2-sshserver/target/$JAR_FULL_NAME $JAR_FULL_PATH
COPY scripts/docker/entrypoint.sh $WORKDIR_PATH
RUN chmod +x $WORKDIR_PATH/entrypoint.sh

ENV WEBSSH_JAR_FULL_PATH=$JAR_FULL_PATH

EXPOSE 5443 5132

ENTRYPOINT ["/app/webssh/entrypoint.sh"]
