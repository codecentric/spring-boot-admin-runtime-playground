# Health Simulator

App to simulate changes in the health status. Spring Boot Admin reports the changed health status in the UI and with browser notifications (if you allow browser notifications).

You can change the number of replicas in the [deployment/values.yaml](deployment/values.yaml). Keep in mind that calls to http://localhost/health-simulator reach a random pod if the number of replicas is greater 0.

## Build App

    mvn clean install

## Build Docker Image

    docker build --tag health-simulator .

## Install App

    helm install health-simulator deployment

### Check deployment

    kubectl get pods -o wide
    kubectl get services -o wide
    kubectl get ingress

### Uninstall

     helm uninstall health-simulator

## Build & Install Script

    chmod u+x buildAndInstall.sh
    ./buildAndInstall.sh

## URI

### Simulated health status

- http://localhost/health-simulator

### Change the health status

- http://localhost/health-simulator/up
- http://localhost/health-simulator/down
- http://localhost/health-simulator/unknown
- http://localhost/health-simulator/out_of_service

## Access Actuator

    kubectl get pods
    kubectl port-forward <pod-name> 8081:8081

### URI

- http://localhost:8081/actuator
