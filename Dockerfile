# Use an OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file into the container
COPY target/almacen-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 8015

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
