# Spring Boot Admin

This is a Spring Boot Admin instance using spring-cloud-starter-kubernetes-discoveryclient for service discovery.
Therefore it needs a running spring-cloud-kubernetes-discoveryserver.
For this reason, the SBA server does not need kubernetes api permissions on kubernetes itself and uses the 
spring-boot-app helmchart.


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
docker build --tag spring-boot-admin:discocli .
```

### Install App
We use the standard "app" helm chart here and NOT the "admin" helm chart as we do not need the k8s api access with this
setup. The discoveryserver has the permissions and is providing the apps via rest api.

```bash
helm upgrade --install spring-boot-admin-discocli ../../helm-charts/spring-boot-app -f deployment/values.yml
```

### Check deployment
```bash
kubectl get pods -o wide
kubectl get services -o wide
kubectl get ingress
```

### Uninstall
```bash
helm uninstall spring-boot-admin-discocli
```

## URI

- http://localhost/spring-boot-admin-kubernetes

## Access Actuator
```bash
kubectl get pods
```
```bash
kubectl port-forward <pod-name> 9091:9071
```
### URI

- http://localhost:9091/actuator
