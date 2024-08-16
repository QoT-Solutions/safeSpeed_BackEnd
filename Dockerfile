FROM openjdk:17-jdk-slim AS build

# Set the working directory
WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Resolve dependencies
RUN ./mvnw dependency:resolve

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw package

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]
