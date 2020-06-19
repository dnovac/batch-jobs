FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD target/batch-jobs-0.0.1-SNAPSHOT.jar batch-jobs.jar
ENTRYPOINT ["java","-jar","batch-jobs.jar"]


