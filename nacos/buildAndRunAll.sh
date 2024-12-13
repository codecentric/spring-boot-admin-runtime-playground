#!/bin/sh

cd apps/spring-boot-admin
./build.sh
cd ../..

cd apps/hello-world
./build.sh
cd ../..

cd apps/health-simulator
./build.sh
cd ../..

docker compose up -d
