#!/bin/sh

# Build App
mvn clean install
# Build Docker Image
docker build --tag spring-boot-admin .
# Install App
helm upgrade --install spring-boot-admin ../../helm-charts/spring-boot-admin
