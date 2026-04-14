# Stage 2: Create a lightweight runtime image
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the generated JAR from the build stage
COPY target/*.jar app.jar

# Cloud Run uses this port
ENV PORT=8080
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","app.jar"]
