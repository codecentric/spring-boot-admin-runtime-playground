# README codecentric

### Build & Push Docker Image
```bash
docker build --platform linux/amd64 -t andreasfritzcodecentric/hello-world:1.0.1 .
docker push andreasfritzcodecentric/hello-world:1.0.1
```

### Install App
```bash
helm upgrade --install hello-world ../../helm-charts/spring-boot-app -f deployment/values-codecentric.yaml
```