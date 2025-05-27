FROM openjdk:23-jdk-slim AS builder

WORKDIR /app

COPY gradle ./gradle
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
COPY src ./src

RUN ./gradlew clean bootJar

FROM openjdk:23-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]