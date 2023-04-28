# Hello World

## Build App

    mvn clean install

## Build Docker Image

    docker build --tag hello-world .

## Install App

    helm install hello-world deployment

### Check deployment

    kubectl get pods -o wide
    kubectl get services -o wide
    kubectl get ingress

### Uninstall

     helm uninstall hello-world

## Build & Install Script

    chmod u+x buildAndInstall.sh
    ./buildAndInstall.sh

## URI

- http://localhost/hello-world

## Access Actuator

    kubectl get pods
    kubectl port-forward <pod-name> 8081:8081

### URI

- http://localhost:8081/actuator
