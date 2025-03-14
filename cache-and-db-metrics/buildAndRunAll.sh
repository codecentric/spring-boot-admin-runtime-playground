#!/bin/sh

cd apps/spring-boot-admin
./build.sh
cd ../..

cd apps/hello-cache
./build.sh
cd ../..

docker-compose up -d
