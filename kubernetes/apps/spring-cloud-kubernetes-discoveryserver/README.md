# Spring Cloud Kubernetes Discovery Server

The official Docker image `springcloud/spring-cloud-kubernetes-discoveryserver` is only published as `*-SNAPSHOT`.
The released artifact on Maven Central is a thin jar (not executable). This module repackages it into an
executable jar and builds a local Docker image from it.

Documentation: https://docs.spring.io/spring-cloud-kubernetes/reference/spring-cloud-kubernetes-discoveryserver.html

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
docker build --tag spring-cloud-kubernetes-discoveryserver .
```

### Install App
```bash
helm upgrade --install spring-cloud-kubernetes-discoveryserver ../../helm-charts/spring-cloud-kubernetes-discoveryserver
```

### Check deployment

```bash
kubectl get pods -o wide
kubectl get services -o wide
```

### Uninstall

```bash
helm uninstall spring-cloud-kubernetes-discoveryserver
```

## Update the version

Change `spring-cloud-kubernetes.version` in `pom.xml` and run `./buildAndInstall.sh` again.

## URI

```bash
kubectl port-forward service/spring-cloud-kubernetes-discoveryserver 8761:80
```

- http://localhost:8761/apps
- http://localhost:8761/actuator
