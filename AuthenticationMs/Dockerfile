FROM openjdk:17-jdk-slim

RUN apt update \
	&& apt install -y curl \
    && apt clean

ARG JAR_FILE=AuthenticationMs-0.0.1-SNAPSHOT.jar

WORKDIR /app

COPY build/libs/${JAR_FILE} /app/AuthenticationMs.jar

ENTRYPOINT ["java", "-jar", "/app/AuthenticationMs.jar"]

