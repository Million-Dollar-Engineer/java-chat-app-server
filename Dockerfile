# Stage 1: Build Stage
FROM openjdk:21-jdk-slim as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the necessary files for Maven build
COPY . .

# Build the Maven project, skipping tests to speed up the build
RUN ./mvnw clean package -Dmaven.test.failure.ignore=true

# Stage 2: Runtime Stage
FROM openjdk:21-jdk-slim as executor

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage to the runtime image
COPY --from=builder /app/.env .env
COPY --from=builder /app/target/ChatApp-0.0.1-SNAPSHOT.jar app.jar

# Specify the command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port that the application listens on
EXPOSE 8881