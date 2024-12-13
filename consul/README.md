## Spring Boot Admin - Consul Example

Allows to run Spring Boot Admin and a sample app (hello world) with service discovery via consul.

This Readme will guide you through all setup steps for the infrastructure.

TODO admin cannot yet call the services, some networking issue needs to be solved

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
Consul UI: http://localhost:8500/

Admin UI: http://localhost:8080/

## Stop Everything
```bash
docker compose down -v
```