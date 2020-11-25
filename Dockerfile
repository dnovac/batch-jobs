# build image from openjdk alpine image
FROM openjdk:14-alpine

# Required for starting application up.
RUN apk update && apk add bash

# Create /opt/app for our java application
RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app

COPY target/batch-jobs-0.0.1-SNAPSHOT.jar $PROJECT_HOME/batch-jobs.jar

EXPOSE 8080

WORKDIR $PROJECT_HOME
#ADD target/batch-jobs-0.0.1-SNAPSHOT.jar batch-jobs.jar
ENTRYPOINT ["java","-jar","batch-jobs.jar"]

CMD ["java", "-Dspring.data.mongodb.uri=mongodb://batchjobs:27017/batchjobs-mongo","-Djava.security.egd=file:/dev/./urandom","-jar","./batch-jobs.jar"]



