FROM maven:3.8.6-openjdk-11 AS build

# Set the working directory
WORKDIR /app

# Copy app source
COPY . .

RUN mvn -N io.takari:maven:wrapper

# Make sure the mvnw script is executable
RUN chmod +x mvnw

# Copy and install custom JAR
COPY libs/ojdbc6-11.2.0.3.jar /app/libs/ojdbc6-11.2.0.3.jar
RUN mvn install:install-file \
    -Dfile=/app/libs/ojdbc6-11.2.0.3.jar \
    -DgroupId=oracle \
    -DartifactId=ojdbc6 \
    -Dversion=11.2.0.3 \
    -Dpackaging=jar

# Compile and package the Spring Boot application
RUN mvn clean install -DskipTests


# Use a smaller image for the runtime
FROM openjdk:11-jre-slim
#
# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/outcome-curr-mgmt/target/outcome-curr-mgmt-1.0-SNAPSHOT.jar /app/outcurr-app.jar

EXPOSE 9092

# Run the Spring Boot app
CMD ["java", "-jar", "/app/outcurr-app.jar"]
