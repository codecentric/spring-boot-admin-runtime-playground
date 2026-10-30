#!/bin/sh
# Usage: ./buildAndRunAll.sh [scenario]   e.g. ./buildAndRunAll.sh notifier-bean
# Set SBA_VERSION to build against another Spring Boot Admin version, e.g. a locally installed snapshot.

cd apps/spring-boot-admin
./build.sh
cd ../..

cd apps/health-simulator
./build.sh
cd ../..

./run.sh "${1:-default}"
