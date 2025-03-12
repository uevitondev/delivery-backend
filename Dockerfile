FROM ubuntu:latest AS build

RUN apt-get update

RUN apt-get install openjdk-21-jdk -y

ENV HOST_EMAIL=''
ENV HOST_EMAIL_PASSWORD=''
ENV AWS_ACCESS_KEY_ID=''
ENV AWS_SECRET_ACCESS_KEY=''

COPY . .

RUN apt-get install maven -y

RUN mvn clean install

FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build  /target/deliverybackend-0.0.1-SNAPSHOT.jar webapp.jar

ENTRYPOINT ["java", "-jar", "webapp.jar"]




