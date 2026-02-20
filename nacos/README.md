## Spring Boot Admin - Nacos Example

Allows to run Spring Boot Admin and two sample apps (hello world, health simulator) with service discovery via Nacos. 

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

http://localhost:8080/

## Stop Everything
```bash
docker compose down -v
```

## Step-by-step Guide

### Start Nacos
```bash
docker run --name nacos-standalone \
  -e MODE=standalone \
  -e NACOS_AUTH_ENABLE=false \
  -e NACOS_AUTH_TOKEN=TmFjb3NTZWNyZXRLZXkyMDI1UGxheWdyb3VuZFRva2Vu \
  -e NACOS_AUTH_IDENTITY_KEY=nacos \
  -e NACOS_AUTH_IDENTITY_VALUE=nacos \
  -p 8848:8848 -p 9848:9848 -d nacos/nacos-server:v3.1.1-slim
```

### Build & Run Apps

- [./apps/hello-world/README.md](./apps/hello-world/README.md)
- [./apps/spring-boot-admin/README.md](./apps/spring-boot-admin/README.md)
- [./apps/health-simulator/README.md](./apps/health-simulator/README.md)

## Installing Other Spring Boot Apps
To let Spring Boot Admin automatically discover another Spring Boot app in this scenario, do the following:

- Add the Nacos discovery starter dependency to your app:
    ```
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        <version>2022.0.0.0-RC2</version>
    </dependency>
    ```
- Make sure `spring.application.name` is set.
- Set `spring.cloud.nacos.discovery.server-addr` to point to the Nacos server:
    ```
    spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
    ```

This will make your app automatically register with Nacos on startup. It will _not_ show up in Spring Boot Admin yet, since the Spring Boot Admin in this example is configured to filter instances by metadata (see `spring.boot.admin.discovery.instances-metadata`).

- Add instance metadata to let Spring Boot Admin discover your app:
    ```
    spring.cloud.nacos.discovery.metadata.spring-boot-admin=true
    ```
