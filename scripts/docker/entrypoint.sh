#!/bin/sh

_sslFile="/app/webssh/websshDockerDefault.p12"
_sslPwd="websshDocker@Admin123!"

if [ ! -f "$_sslFile" ]; then
keytool -genkey -alias springboottomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore $_sslFile -validity 3650 -dname "CN=webssh, OU=NoOU, O=NoO, L=Chendu, ST=Sichuan, C=cn" -storepass "$_sslPwd"  -keypass "$_sslPwd"
fi

sh -c "java -server  ${JAVA_OPTS} -jar ${WEBSSH_JAR_FULL_PATH} ${SPRING_BOOT_OPTS}"
