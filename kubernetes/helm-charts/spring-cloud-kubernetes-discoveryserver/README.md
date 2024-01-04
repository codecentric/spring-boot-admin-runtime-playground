# Spring Cloud Kubernetes Discovery Server

## Documentation

https://docs.spring.io/spring-cloud-kubernetes/reference/spring-cloud-kubernetes-discoveryserver.html

## Install Spring Cloud Kubernetes Discovery Server
```bash
helm upgrade --install spring-cloud-kubernetes-discoveryserver .
```

### Check deployment
```bash
kubectl get pods -o wide
kubectl get services -o wide
kubectl get serviceaccounts
kubectl get role
kubectl get rolebinding -o wide
```

### Uninstall
```bash
helm uninstall spring-cloud-kubernetes-discoveryserver
```

## Access Actuator
```bash
kubectl get pods
```
```bash
kubectl port-forward service/spring-cloud-kubernetes-discoveryserver 8761:80
```
### URI

- http://localhost:8761/apps
- http://localhost:8761/actuator
