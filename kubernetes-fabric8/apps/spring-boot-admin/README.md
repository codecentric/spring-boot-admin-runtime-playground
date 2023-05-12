# Spring Boot Admin

## Build App
```bash
mvn clean install
```

## Build Docker Image
```bash
docker build --tag spring-boot-admin .
```

## Install App
```bash
helm install spring-boot-admin ../../helm-charts/spring-boot-admin
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
helm uninstall spring-boot-admin
```

## Build & Install Script
```bash
chmod u+x buildAndInstall.sh
./buildAndInstall.sh
```

## URI

- http://localhost/spring-boot-admin

## Access Actuator
```bash
kubectl get pods
```
```bash
kubectl port-forward <pod-name> 9091:8081
```
### URI

- http://localhost:9091/actuator
