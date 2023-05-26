# README codecentric

### Build & Push Docker Image
```bash
docker build --platform linux/amd64 -t andreasfritzcodecentric/health-simulator:1.0.0 .
docker push andreasfritzcodecentric/health-simulator:1.0.0
```

### Install App
```bash
helm upgrade --install health-simulator ../../helm-charts/spring-boot-app -f deployment/values-codecentric.yaml
```
