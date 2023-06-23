#!/bin/sh

# Build App
mvn clean install
# Build Docker Image
docker build --tag hello-world .
# Install App
helm upgrade --install hello-world ../../helm-charts/spring-boot-app -f deployment/values.yaml
