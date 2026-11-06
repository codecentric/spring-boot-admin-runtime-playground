# Health Simulator

App to simulate changes in health status. It registers itself with Spring Boot Admin (SBA client) and counts which
SBA node polls it.

## Build Script

```bash
chmod u+x build.sh
./build.sh
```

## Run
This assumes that Spring Boot Admin is reachable on port 8080 of your machine.
```bash
docker run --name health-simulator -e SBA_URL=http://host.docker.internal:8080 -p 8281:8080 -d --rm health-simulator-hazelcast
```

## URI

### Simulated health status

- http://localhost:8281/

### Change the health status

- http://localhost:8281/up
- http://localhost:8281/down
- http://localhost:8281/unknown
- http://localhost:8281/out_of_service

### Requests per SBA node

- `GET` http://localhost:8281/pollers
- `DELETE` http://localhost:8281/pollers resets the counters

### Access Actuator

- http://localhost:8281/actuator
