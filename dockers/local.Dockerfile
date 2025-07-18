# Dockerfile.local
FROM openjdk:17-jdk-slim
VOLUME /tmp
WORKDIR /app
COPY . .
ENTRYPOINT ["./gradlew", "bootRun"]