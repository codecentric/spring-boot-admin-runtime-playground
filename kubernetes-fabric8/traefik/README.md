# Traefik

## Documentation

https://doc.traefik.io/traefik/getting-started/quick-start-with-kubernetes/

## Install Traefik

    helm install traefik .

### Check deployment

    kubectl get pods -o wide
    kubectl get services -o wide
    kubectl get accounts
    kubectl get clusterrole
    kubectl get clusterrolebinding kubectl -o wide

### Uninstall

     helm uninstall traefik

## Traefik Dashboard

- http://localhost:9090
- the dashboard will show that it is listening to 8080, as this is the internal port that will be mapped to 9090 in k8s
