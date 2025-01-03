## Spring Boot Admin - Eureka Example

Allows to run Spring Boot Admin and two sample apps (hello world, health simulator) with service discovery via eureka. 

This Readme will guide you through all setup steps for the infrastructure.

## Prerequisites 

- Java
- Maven
- Docker and Docker Compose

## Run Everything
You can run the whole build and start all apps in docker containers with the following script or follow the step-by-step guide below.
```bash
chmod u+x buildAndRunAll.sh
./buildAndRunAll.sh
```
Eureka UI: http://localhost:8761/

Admin UI: http://localhost:8080/

## Stop Everything
```bash
docker compose down -v
```
