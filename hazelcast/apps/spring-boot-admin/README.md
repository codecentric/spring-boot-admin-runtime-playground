# Spring Boot Admin

Spring Boot Admin server with Hazelcast clustering. See [../../README.md](../../README.md) for the scenarios.

## Build Script

```bash
chmod u+x build.sh
./build.sh
# against another SBA version, e.g. a locally installed snapshot
SBA_VERSION=4.1.5-SNAPSHOT ./build.sh
```

## Step-by-step build

### Build App
```bash
mvn package
```

### Build Docker Image
```bash
docker build --tag spring-boot-admin-hazelcast .
```

### Run locally

Start several instances on different ports. Without `HAZELCAST_MEMBERS` they find each other via multicast.
```bash
NODE_NAME=sba-1 java -jar target/app.jar --server.port=8091 --management.server.port=8191
NODE_NAME=sba-2 java -jar target/app.jar --server.port=8092 --management.server.port=8192
```

## Playground switches

| Property | Values | Default |
|---|---|---|
| `playground.event-store` | `hazelcast`, `in-memory` | `hazelcast` |
| `playground.notifier` | `properties`, `bean` | `properties` |
| `playground.sharding.enabled` | `true`, `false` | `false` |
| `playground.hazelcast.members` | comma separated hosts, empty = multicast | empty |
| `playground.hazelcast.event-store-ttl-seconds` | seconds, 0 = none | `0` |
| `playground.hazelcast.sent-notifications-ttl-seconds` | seconds, 0 = none | `0` |

## URI

- http://localhost:8080/
- http://localhost:8081/actuator/playground
