# Hello World

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
docker build --tag hello-world .
```

### Install App
```bash
helm upgrade --install hello-world ../../helm-charts/spring-boot-app -f deployment/values.yaml
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
