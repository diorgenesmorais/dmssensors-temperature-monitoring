#!/bin/sh
set -e

dockerize -wait tcp://${SPRING_RABBITMQ_HOST}:${SPRING_RABBITMQ_PORT} -timeout 60s

exec java -jar "$JAR_NAME"
