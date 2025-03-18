FROM openjdk:17-jdk-slim

ARG JAR_FILE=Gateway-0.0.1-SNAPSHOT.jar

WORKDIR /app

COPY build/libs/${JAR_FILE} /app/Gateway.jar

ENTRYPOINT ["java", "-jar", "/app/Gateway.jar"]






