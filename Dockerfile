# Use lightweight Java image
FROM openjdk:27-ea-trixie

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/*.jar app.jar

# Expose port (change if needed)
EXPOSE 8080

# Run app
ENTRYPOINT ["java","-jar","app.jar"]