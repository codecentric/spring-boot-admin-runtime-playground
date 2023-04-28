#!/bin/sh

# Uninstall App
helm uninstall spring-boot-admin
# Build App
mvn clean install
# Build Docker Image
docker build --tag spring-boot-admin .
# Install App
helm install spring-boot-admin deployment
