# Quartz Shedlock Migration

## Overview

Quartz Shedlock Migration is a Java/Spring Boot project that utilizes
Quartz Scheduler and Shedlock to provide distributed lock functionality
for scheduled tasks. This project is built using Maven and requires JDK 21 or later.

## Features

- Integration of Quartz Scheduler for job scheduling.
- Use of Shedlock for distributed lock management.
- Maven for project build and dependency management.
- Developed and tested with JDK 21.

## Prerequisites

Make sure you have the following installed on your machine:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) 21 or later
- [Maven](https://maven.apache.org/download.cgi)
- [GraalVM Native Image](https://www.graalvm.org)
- [Docker](https://www.docker.com)

## Getting Started with Maven

**1. Clone the repository:**

   ```bash
   git clone https://github.com/korkutkose/quartz-shedlock-migration.git
   cd quartz-shedlock-migration
   ```

**2. Install and Run The Application:**

   ```bash
   mvn clean install
   mvn spring-boot:run 
   ```

**3. Enable Different Profile**

* By default, the application runs with the `shedlock` profile.
* To enable the `quartz` profile, you can use the following command:
  ```bash
  mvn spring-boot:run -Dspring-boot.run.profiles=quartz
  ```

**4. Build Native Image Locally**

> Do not forget to install and set the `GRAALVM_HOME` environment variable to the GraalVM installation directory.

* To build a native image of the application, you can use the following command:
  ```bash
  mvn clean package -Pnative native:compile
  ```
* To run the native image, you can use the following command:
  ```bash
  ./target/native-executable-app 
  ```
* To run the native image with profile, you can use the following command:
  ```bash
  ./target/native-executable-app --spring.profiles.active=quartz
  ```

## Getting Started with Docker

> Do not forget to install Docker on your machine and make sure that the Docker daemon is running.

1. Run docker-compose to build and run the application:
   > This will take some time to build since this will be a multi-stage GraalVM native build.
    ```bash
    docker-compose -f docker-compose.yml -p quartz-shedlock-migration up -d
    ```
2. You can also provide additional profile while running the docker-compose command:
    ```bash
    APP_ACTIVE_PROFILE=quartz docker-compose -f docker-compose.yml -p quartz-shedlock-migration up -d
    ```
3. Check the logs to see if the application is running:
    ```bash
    docker-compose -f docker-compose.yml -p quartz-shedlock-migration logs -f
    ```
4. To stop the application, you can use the following command:
    ```bash
    docker-compose -f docker-compose.yml -p quartz-shedlock-migration down
   ```

## Contact

If you want to contact me you can reach me at;

* [Linkedin](https://www.linkedin.com/in/korkutkose/)
* [korkutkose.com](https://korkutkose.com)
* [E-Mail](mailto:korkut_kose@hotmail.com)
