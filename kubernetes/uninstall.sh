#!/bin/sh

helm uninstall hello-world
helm uninstall spring-boot-admin-fabric8
helm uninstall spring-boot-admin-kubernetes
helm uninstall spring-boot-admin-discoveryclient
helm uninstall health-simulator
helm uninstall traefik
helm uninstall spring-cloud-kubernetes-discoveryserver
