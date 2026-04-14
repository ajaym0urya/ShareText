# Use stable lightweight Java
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/app.jar app.jar

# Cloud Run uses this port
ENV PORT=8080
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","app.jar"]
