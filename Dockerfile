FROM openjdk:14-alpine

EXPOSE 8080

# run app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} batch-jobs.jar
ENTRYPOINT ["java","-jar","/batch-jobs.jar"]


