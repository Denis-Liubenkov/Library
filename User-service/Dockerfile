FROM openjdk:17-jdk-slim

ARG JAR_FILE=User-service-0.0.1-SNAPSHOT.jar

WORKDIR /app

COPY build/libs/${JAR_FILE} /app/User-service.jar

ENTRYPOINT ["java", "-jar", "/app/User-service.jar"]












