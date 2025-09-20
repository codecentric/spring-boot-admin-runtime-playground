# Sample for running using Hazelcast.

- http://localhost:8080 -> Spring Boot Main Application
- http://localhost:8090 -> Spring Boot Replica Application

![img.png](img.png)

## Prerequisites

- Java
- Maven
- Docker and Docker Compose

## Run Everything
You can run the whole build and start all apps in docker containers with the following script
```bash
chmod u+x buildAndRunAll.sh
./buildAndRunAll.sh
```

## Stop Everything
```bash
docker compose down -v
```