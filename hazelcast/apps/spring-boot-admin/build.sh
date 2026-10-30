#!/bin/sh

# Build App (set SBA_VERSION to test a locally installed snapshot, e.g. SBA_VERSION=4.1.5-SNAPSHOT)
mvn package ${SBA_VERSION:+-Dspring-boot-admin.version=$SBA_VERSION}
# Build Docker Image
docker build --tag spring-boot-admin-hazelcast .
