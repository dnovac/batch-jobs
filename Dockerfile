FROM openjdk:14-alpine
VOLUME /tmp
EXPOSE 8080
ADD target/batch-jobs-0.0.1-SNAPSHOT.jar batch-jobs.jar
ENTRYPOINT ["java","-Dspring.data.mongodb.host=mongo","-Dspring.data.mongodb.database=batchjobs","-Dspring.data.mongodb.port=27017","-Djava.security.egd=file:/dev/./urandom","-jar","batch-jobs.jar"]



