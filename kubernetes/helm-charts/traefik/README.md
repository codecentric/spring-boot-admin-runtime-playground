# Traefik

## Documentation

https://doc.traefik.io/traefik/getting-started/quick-start-with-kubernetes/

## Install Traefik
```bash
helm upgrade --install traefik . -f values.yaml
```

### Check deployment
```bash
kubectl get pods -o wide
kubectl get services -o wide
kubectl get serviceaccounts
kubectl get clusterrole
kubectl get clusterrolebinding -o wide
```

### Uninstall
```bash
helm uninstall traefik
```

## Traefik Dashboard

- http://localhost:9090
- the dashboard will show that it is listening to 8080, as this is the internal port that will be mapped to 9090 in k8s
