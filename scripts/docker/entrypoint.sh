#!/bin/sh


sh -c "java -server  ${JAVA_OPTS} -jar ${JAR_FULL_PATH} ${SPRING_BOOT_OPTS}"
