FROM docker.io/amazoncorretto:21-alpine
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8085
CMD ["java", "-XX:MaxRAMPercentage", "90.0", "-jar", "app.jar"]
