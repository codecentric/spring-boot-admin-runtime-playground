#!/bin/sh

# Uninstall App
helm uninstall hello-world
# Build App
mvn clean install
# Build Docker Image
docker build --tag hello-world .
# Install App
helm install hello-world ../spring-boot-app-helmchart -f deployment/values.yaml
