# Health Simulator

App to simulate changes in health status. Spring Boot Admin reports these in the UI and with browser notifications (if you allow browser notifications).

You can change the number of replicas in [deployment/values.yaml](deployment/values.yaml). Note that calls to http://localhost/health-simulator will reach a random pod if the number of replicas is greater than 0. Calls to http://localhost/health-simulator/up ... change the status of a random pod.

## Build App
```bash
mvn clean install
```

## Build Docker Image
```bash
docker build --tag health-simulator .
```

## Install App
```bash
helm install health-simulator ../../helm-charts/spring-boot-app -f deployment/values.yaml
```

### Check deployment
```bash
kubectl get pods -o wide
kubectl get services -o wide
kubectl get ingress
```

### Uninstall
```bash
helm uninstall health-simulator
```

## Build & Install Script
```bash
chmod u+x buildAndInstall.sh
./buildAndInstall.sh
```

## URI

### Simulated health status

- http://localhost/health-simulator

### Change the health status

- http://localhost/health-simulator/up
- http://localhost/health-simulator/down
- http://localhost/health-simulator/unknown
- http://localhost/health-simulator/out_of_service

## Access Actuator
```bash
kubectl get pods
```    
```bash
kubectl port-forward <pod-name> 8081:8081
```
### URI

- http://localhost:8081/actuator
