# Spring Boot Admin

## Build App

    mvn clean install

## Build Docker Image

    docker build --tag spring-boot-admin .

## Install App

    helm install spring-boot-admin deployment

### Check deployment

    kubectl get pods -o wide
    kubectl get services -o wide
    kubectl get ingress
    kubectl get serviceaccount
    kubectl get role
    kubectl get rolebinding -o wide

### Uninstall

     helm uninstall spring-boot-admin

## Build & Install Script

    chmod u+x buildAndInstall.sh
    ./buildAndInstall.sh

## URI

- http://localhost/spring-boot-admin

## Access Actuator

    kubectl get pods
    kubectl port-forward <pod-name> 9091:8081

### URI

- http://localhost:9091/actuator
