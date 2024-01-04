#!/bin/sh

helm upgrade --install traefik helm-charts/traefik

cd apps/spring-boot-admin-kubernetes
./buildAndInstall.sh
cd ../..

cd apps/spring-boot-admin-fabric8
./buildAndInstall.sh
cd ../..

helm upgrade --install spring-cloud-kubernetes-discoveryserver helm-charts/spring-cloud-kubernetes-discoveryserver

cd apps/spring-boot-admin-discoveryclient
./buildAndInstall.sh
cd ../..

cd apps/hello-world
./buildAndInstall.sh
cd ../..

cd apps/health-simulator
./buildAndInstall.sh
cd ../..
