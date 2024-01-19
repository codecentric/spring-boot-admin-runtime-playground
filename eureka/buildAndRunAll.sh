#!/bin/sh

cd apps/eureka
./build.sh
cd ../..

cd apps/spring-boot-admin
./build.sh
cd ../..

cd apps/hello-world
./build.sh
cd ../..

docker-compose up -d
