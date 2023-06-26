# Spring Boot Admin

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
docker build --tag spring-boot-admin-nacos .
```

### Run
This assumes, that you have Nacos running on its default port on your local machine.
```bash
docker run --name spring-boot-admin-nacos -e NACOS_SERVER_ADDR=host.docker.internal:8848 -p 8080:8080 -p 8081:8081 -d --rm spring-boot-admin-nacos
```

## URI

- http://localhost:8080/

## Access Actuator
### URI

- http://localhost:8081/actuator
