Sample for [Spring Cloud Bus](https://docs.spring.io/spring-cloud-bus/docs/current/reference/html/)

Starts 
- RabbitMQ (http://localhost:15672, credentials: guest/guest)
- a Spring Boot Admin Server (http://localhost:8080)
- a Hello World app (http://localhost:8081)

If a bus-refresh is triggered on one of the spring boot apps, spring cloud sends a message to all other apps to 
refresh their configuration (visible in the logs, currently there is nothing to refresh).

Bus-refresh is now also available in the Spring Boot Admin UI on the environment page. 
Bus-env and bus-shutdown are not yet supported.

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