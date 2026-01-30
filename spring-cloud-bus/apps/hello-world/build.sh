#!/bin/sh

# Build App
mvn package
# Build Docker Image
docker build --tag hello-world-cloudbus .
