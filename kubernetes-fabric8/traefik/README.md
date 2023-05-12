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

## URI

- http://localhost:9090
