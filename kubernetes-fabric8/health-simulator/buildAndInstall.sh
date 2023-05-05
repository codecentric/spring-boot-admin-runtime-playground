#!/bin/sh

# Uninstall App
helm uninstall health-simulator
# Build App
mvn clean install
# Build Docker Image
docker build --tag health-simulator .
# Install App
helm install health-simulator deployment
