## Spring Boot Admin - Kubernetes Example

Allows to run Spring Boot Admin and two sample apps (hello world, health simulator) in kubernetes. 

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

    kubectl config get-contexts
    kubectl config use-context docker-desktop

## Install Helm

    brew install helm
    helm version

### Documentation

- <https://helm.sh/docs/intro/quickstart/>

## Install Traefik

- [./helm-charts/traefik/README.md](./helm-charts/traefik/README.md)

## Build & Install Apps

- [./apps/hello-world/README.md](./apps/hello-world/README.md)
- [./apps/spring-boot-admin/README.md](./apps/spring-boot-admin/README.md)
- [./apps/health-simulator/README.md](./apps/health-simulator/README.md)

## Uninstall Apps
```bash
chmod u+x uninstall.sh
./uninstall.sh
```   