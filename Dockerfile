# Use a compatible Gradle image
FROM gradle:8.5-jdk21 AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Run the Gradle build
RUN gradle clean build --no-daemon

# Use the OpenJDK runtime for the application
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
