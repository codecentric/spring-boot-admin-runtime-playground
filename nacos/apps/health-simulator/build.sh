#!/bin/sh

# Build App
mvn package
# Build Docker Image
docker build --tag health-simulator-nacos .
