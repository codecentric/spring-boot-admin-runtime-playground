#!/bin/sh

# Build App
mvn clean install
# Build Docker Image
docker build --tag spring-boot-admin:kubernetes .
# Install App
helm upgrade --install spring-boot-admin-kubernetes ../../helm-charts/spring-boot-admin -f deployment/values.yml
