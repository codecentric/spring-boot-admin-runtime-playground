#!/bin/sh

# Build App
mvn clean install
# Build Docker Image
docker build --tag health-simulator .
# Install App
helm upgrade --install health-simulator ../../helm-charts/spring-boot-app -f deployment/values.yaml
