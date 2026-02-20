# Health Simulator

App to simulate changes in health status. Spring Boot Admin reports these in the UI and with browser notifications (if you allow browser notifications).

## Build Script

You can run the whole build with the following script or follow the step-by-step guide below.

```bash
chmod u+x build.sh
./build.sh
```

## Step-by-step build

### Build App
```bash
mvn package
```

### Build Docker Image
```bash
docker build --tag health-simulator-nacos .
```

### Run
This assumes, that you have Nacos running on its default port on your local machine. 
```bash
docker run --name health-simulator-nacos -e NACOS_SERVER_ADDR=host.docker.internal:8848 -p 8280:8080 -p 8281:8081 -d --rm health-simulator-nacos
```

## URI

### Simulated health status

- http://localhost:8280/

### Change the health status

- http://localhost:8280/up
- http://localhost:8280/down
- http://localhost:8280/unknown
- http://localhost:8280/out_of_service

### Access Actuator

- http://localhost:8281/actuator
