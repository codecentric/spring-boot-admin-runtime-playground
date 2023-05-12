# Hello World

## Build App
```bash
mvn clean install
```

## Build Docker Image
```bash
docker build --tag hello-world .
```

## Install App
```bash
helm install hello-world ../spring-boot-app-helmchart -f deployment/values.yaml
```

### Check deployment
```bash
kubectl get pods -o wide
kubectl get services -o wide
kubectl get ingress
```

### Uninstall
```bash
helm uninstall hello-world
```

## Build & Install Script
```bash
chmod u+x buildAndInstall.sh
./buildAndInstall.sh
```

## URI

- http://localhost/hello-world

## Access Actuator
```bash
kubectl get pods
```
```bash
kubectl port-forward <pod-name> 8081:8081
```
### URI

- http://localhost:8081/actuator
