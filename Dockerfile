# Stage 1: Build Stage
FROM openjdk:21-jdk-slim as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the necessary files for Maven build
COPY . .

# Build the Maven project, skipping tests to speed up the build
RUN ./mvnw clean package -Dmaven.test.failure.ignore=true
RUN cat jar-to-bin.sh target/ChatApp-0.0.1-SNAPSHOT.jar > app && chmod 777 app

# Stage 2: Runtime Stage
FROM openjdk:21-jdk-slim as executor

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage to the runtime image
COPY --from=builder /app/.env .env
COPY --from=builder /app/app app
RUN chmod +x app

# Specify the command to run the application when the container starts
ENTRYPOINT ["./app"]

# Expose the port that the application listens on
EXPOSE 8888