#!/bin/sh

# Build App
mvn clean install
# Build Docker Image
docker build --tag spring-boot-admin:fabric8 .
# Install App
helm upgrade --install spring-boot-admin-fabric8 ../../helm-charts/spring-boot-admin -f deployment/values.yml
