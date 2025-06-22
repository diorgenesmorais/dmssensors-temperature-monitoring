FROM gradle:jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar

FROM eclipse-temurin:21-jre-jammy
ARG SERVER_PORT=8082
ARG JAR_NAME=temperature-monitoring.jar
ARG TZ=America/Sao_Paulo
ARG SPRING_RABBITMQ_HOST=dmssensors-rabbitmq
ARG SPRING_RABBITMQ_PORT=5672
ENV SERVER_PORT=${SERVER_PORT} \
    JAR_NAME=${JAR_NAME} \
    TZ=${TZ} \
    DOCKERIZE_VERSION=v0.9.3 \
    SPRING_RABBITMQ_HOST=${SPRING_RABBITMQ_HOST} \
    SPRING_RABBITMQ_PORT=${SPRING_RABBITMQ_PORT}
RUN apt-get update && \
    apt-get install -y wget && \
    wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin && \
    apt-get autoremove -yqq --purge wget && rm -rf /var/lib/apt/lists/* && \
    groupadd -r appuser && useradd -r -g appuser appuser && \
    mkdir -p /home/appuser && chown -R appuser:appuser /home/appuser
WORKDIR /app
COPY --from=build /app/build/libs/$JAR_NAME .
COPY entrypoint.sh .
RUN chmod +x entrypoint.sh
USER appuser
HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:$SERVER_PORT/actuator/health | grep 'UP' || exit 1
EXPOSE $SERVER_PORT
ENTRYPOINT ["./entrypoint.sh"]
