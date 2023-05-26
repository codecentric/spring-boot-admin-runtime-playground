# README codecentric

### Build & Push Docker Image
```bash
docker build --platform linux/amd64 -t andreasfritzcodecentric/spring-boot-admin:1.0.1 .
docker push andreasfritzcodecentric/spring-boot-admin:1.0.1
```

### Install App
```bash
helm upgrade --install spring-boot-admin ../../helm-charts/spring-boot-admin -f deployment/values-codecentric.yaml
```
