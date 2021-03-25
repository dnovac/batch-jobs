## About
This piece of software, developed using Java, Spring Boot and Spring Batch, exposes an API
which handles import of CSV files and XML files into a NoSQL MongoDB database.

The documentation for the API can be found by accessing swagger link: `localhost:8080/swagger-ui.html`, 
only after the application is deployed on the local computer.

## Pre-requisites
- install java if not installed: https://openjdk.java.net/install/
- install maven if not installed: https://maven.apache.org/install.html
- install Docker desktop and run it: https://www.docker.com/products/docker-desktop

## Deploy
Run these commands in order to build the app, and deploy it in a container, using Docker and docker-compose

* `mvn clean install -DskipTests=true` - builds the app and creates .jar file in the `./target` folder

* `docker network create main-network` - create the network needed for the java and mongo containers to communicate

* `docker-compose build`

* `docker-compose up -d` - run the app

## Info
 - The app is running on local port 8080
 - The configuration can be found in application.properties
 - I provided two files for testing purposes. Location: `/src/main/resources/test-data`
 - The import for the csv file from test-data, netflix_titles.csv took ~1s for 6k+ lines of data

## Postman collection
 * The Postman collection with the API request can be downloaded here: https://www.getpostman.com/collections/f90c5bd63f0393831a26

Thanks for sharing and if you want to make it better feel free to open merge-requests :)

