# Spring Boot Admin

This is a Spring Boot Admin instance using spring-cloud-starter-kubernetes-fabric8 for service discovery. 

## Build & Install Script

You can run the whole build and installation with the following script or follow the step-by-step guide below.

```bash
chmod u+x buildAndInstall.sh
./buildAndInstall.sh
```

## Step-by-step build

### Build App
```bash
mvn clean install
```

### Build Docker Image
```bash
docker build --tag spring-boot-admin:fabric8 .
```

### Install App
```bash
helm upgrade --install spring-boot-admin-fabric8 ../../helm-charts/spring-boot-admin -f deployment/values.yml
```

### Check deployment
```bash
kubectl get pods -o wide
kubectl get services -o wide
kubectl get ingress
kubectl get serviceaccount
kubectl get role
kubectl get rolebinding -o wide
```

### Uninstall
```bash
helm uninstall spring-boot-admin-fabric8
```

## URI

- http://localhost/spring-boot-admin-fabric8 (requires [traefik](../../helm-charts/traefik/README.md) to be running)

## Access Actuator
```bash
kubectl get pods
```
```bash
kubectl port-forward <pod-name> 9091:8081
```
### URI

- http://localhost:9091/actuator
