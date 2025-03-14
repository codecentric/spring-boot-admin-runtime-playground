## Spring Boot Admin - Cache and DB metrics Example

Allows to run Spring Boot Admin and one sample app (hello-data) which is connected to redis and h2 database and provides
metrics for these data sources.

This Readme will guide you through all setup steps for the infrastructure.

## Prerequisites 

- Java
- Maven
- Docker and Docker Compose

## Run Everything
You can run the whole build and start all apps in docker containers with the following script.
```bash
chmod u+x buildAndRunAll.sh
./buildAndRunAll.sh
```
Trigger Cache: http://localhost:8180/trigger-cache
Trigger DB: http://localhost:8180/trigger-db

Admin UI: http://localhost:8080/

## Stop Everything
```bash
docker compose down -v
```
