# Spring Cloud Kubernetes Discovery Server

## Documentation

https://docs.spring.io/spring-cloud-kubernetes/reference/spring-cloud-kubernetes-discoveryserver.html

## Install Spring Cloud Kubernetes Discovery Server
The image is built locally, see [../../apps/spring-cloud-kubernetes-discoveryserver/README.md](../../apps/spring-cloud-kubernetes-discoveryserver/README.md).
Build and install in one step:
```bash
cd ../../apps/spring-cloud-kubernetes-discoveryserver
./buildAndInstall.sh
```

Install only (image must already exist):
```bash
helm upgrade --install spring-cloud-kubernetes-discoveryserver . -f values.yaml
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

## Access Endpoints or Actuator

```bash
kubectl port-forward service/spring-cloud-kubernetes-discoveryserver 8761:80
```

### URI

- http://localhost:8761/apps
- http://localhost:8761/actuator
