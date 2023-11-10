#!/bin/sh

# Build App
mvn clean install
# Build Docker Image
docker build --tag spring-boot-admin:discocli .
# Install App
helm upgrade --install spring-boot-admin-discocli ../../helm-charts/spring-boot-app -f deployment/values.yml
