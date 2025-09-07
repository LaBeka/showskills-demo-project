FROM maven:latest as builder
COPY pom.xml .
WORKDIR .

RUN mvn clean install

FROM openjdk:21-oracle
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo-project.jar"]