## Spring Boot Admin - Kubernetes Example

Allows to run Spring Boot Admin and two sample apps (hello world, health simulator) in kubernetes. 

There are 2 instances of Spring Boot Admin available. Both use the discovery mechanism to find the apps to monitor, 
there is no self registration used. One SBA is using the spring cloud kubernetes-client and the other one
the spring cloud kubernetes-fabric8. There are small differences in the configuration between these two versions.

This Readme will guide you through all setup steps for the infrastructure.

## Enable Kubernetes in Docker Desktop

1. From the Docker Dashboard, select the **Settings**.
2. Select **Kubernetes** from the left sidebar.
3. Next to **Enable Kubernetes**, select the checkbox.
4. Select **Apply & Restart** to save the settings and then click **Install** to confirm. This instantiates images required to run the Kubernetes server as containers, and installs the `/usr/local/bin/kubectl` command on your machine.

### Documentation

- <https://docs.docker.com/desktop/kubernetes/>
- <https://kubernetes.io/docs/reference/kubectl/cheatsheet/>

### Change Kubernetes Context
```bash
kubectl config get-contexts
kubectl config use-context docker-desktop
```

## Install Helm
```bash
brew install helm
helm version
```

### Documentation

- <https://helm.sh/docs/intro/quickstart/>

## Install Everything
You can run the whole build and installation for all apps with the following script or follow the step by step guide below.
```bash
chmod u+x buildAndInstallAll.sh
./buildAndInstallAll.sh
```

http://localhost/spring-boot-admin-kubernetes

http://localhost/spring-boot-admin-fabric8

## Step-by-step Installation

### Install Traefik

- [./helm-charts/traefik/README.md](./helm-charts/traefik/README.md)

### Build & Install Apps

- [./apps/hello-world/README.md](./apps/hello-world/README.md)
- [./apps/spring-boot-admin-kubernetes/README.md](./apps/spring-boot-admin-kubernetes/README.md)
- [./apps/spring-boot-admin-fabric8/README.md](./apps/spring-boot-admin-fabric8/README.md)
- [./apps/health-simulator/README.md](./apps/health-simulator/README.md)

## Uninstall Everything
```bash
chmod u+x uninstall.sh
./uninstall.sh
```

## Installing Other Spring Boot Apps

You can install any Spring Boot App using the helm chart (your app must be available as docker image).

```bash
helm upgrade --install <your-app-name> helm-charts/spring-boot-app -f <path-to-your>/values.yaml
```
OR
```bash
helm upgrade --install <your-app-name> helm-charts/spring-boot-app --set name=<your-spring-app> --set deployment.image=<yourImage>
```

```bash
helm uninstall <your-app-name>
```
