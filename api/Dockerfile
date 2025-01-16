FROM eclipse-temurin:21 AS build

WORKDIR /app
COPY . .
RUN ./mvnw clean package


FROM eclipse-temurin:21-jre AS release
# Set the working directory
WORKDIR /app

# Copy the jar file
COPY --from=build /app/target/purrchase-1.0-SNAPSHOT.jar app.jar

# Documentation on port to expose
EXPOSE 8080

# Set the entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]