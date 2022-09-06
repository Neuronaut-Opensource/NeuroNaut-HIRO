#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY ./backend/src /home/app/src
COPY ./backend/pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dspring.config.name=docker

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/inventory-0.0.1-SNAPSHOT.jar /usr/local/lib/inventory.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/inventory.jar"]