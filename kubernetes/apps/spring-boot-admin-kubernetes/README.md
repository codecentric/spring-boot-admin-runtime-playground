# Spring Boot Admin

## Build & Install Script

You can run the whole build and installation with the following script or follow the step by step guide below.

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
docker build --tag spring-boot-admin:kubernetes .
```

### Install App
```bash
helm upgrade --install spring-boot-admin-kubernetes ../../helm-charts/spring-boot-admin -f deployment/values.yml
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
helm uninstall spring-boot-admin-kubernetes
```

## URI

- http://localhost/spring-boot-admin-kubernetes

## Access Actuator
```bash
kubectl get pods
```
```bash
kubectl port-forward <pod-name> 9091:9091
```
### URI

- http://localhost:9091/actuator
