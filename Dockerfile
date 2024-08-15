FROM maven:3.9.4-openjdk-22 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:22-jdk-slim
COPY --from=build /target/springboot-mongo-atlas-0.0.1-SNAPSHOT.jar springboot-mongo-atlas.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","springboot-mongo-atlas.jar"]