# build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# runtime stage
FROM eclipse-temurin:17-jdk
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]