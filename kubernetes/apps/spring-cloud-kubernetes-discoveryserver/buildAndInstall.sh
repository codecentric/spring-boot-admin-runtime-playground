#!/bin/sh

# Build App
mvn clean install
# Build Docker Image
docker build --tag spring-cloud-kubernetes-discoveryserver .
# Install App
helm upgrade --install spring-cloud-kubernetes-discoveryserver ../../helm-charts/spring-cloud-kubernetes-discoveryserver
