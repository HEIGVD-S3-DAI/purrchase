# Deploy

<!-- mtoc-start -->

* [Publish the docker image](#publish-the-docker-image)
  * [Create a personal access token](#create-a-personal-access-token)
  * [Login to GitHub Container Registry](#login-to-github-container-registry)
  * [Tag the image correctly for GitHub Container Registry](#tag-the-image-correctly-for-github-container-registry)
  * [Publish the image on GitHub Container Registry](#publish-the-image-on-github-container-registry)
  * [Connect the package to the repository](#connect-the-package-to-the-repository)
* [Setup virtual machine](#setup-virtual-machine)
  * [Create a virtual machine](#create-a-virtual-machine)
  * [Connect to the virtual machine](#connect-to-the-virtual-machine)
  * [Update and secure the virtual machine](#update-and-secure-the-virtual-machine)
  * [Install and configure Docker and Docker Compose](#install-and-configure-docker-and-docker-compose)
    * [Check the installation](#check-the-installation)
  * [Install `apache2-utils`](#install-apache2-utils)
* [Deploy on the virtual machine](#deploy-on-the-virtual-machine)
  * [Obtain a domain name](#obtain-a-domain-name)
  * [Configure Traefik](#configure-traefik)
    * [Create a user](#create-a-user)
    * [Setup environments variables](#setup-environments-variables)
  * [Start Traefik](#start-traefik)
  * [Start the API](#start-the-api)
* [Summary](#summary)

<!-- mtoc-end -->

The file explains how to deploy the project from publishing the docker image,
creating the virtual machine to running the containers.

It assumes your have an Azure account, but you can use any other cloud provider
that allows you to create a single virtual machine. Make sure to use the same
image.

## Publish the docker image

First we need to publish the docker image to GitHub's container registry.

For this navigate to the `api/` directory and build the docker image with:

```bash
docker build -t purrchase:latest .
```

> [!NOTE]
>
> For those on a MacOS ARM architecture, run
> `docker build --platform=linux/amd64/v2 -t purrchase:latest .` instead.

### Create a personal access token

You will need a personal access token to publish an image on GitHub Container
Registry.

A personal access token is a token that you can use to authenticate to GitHub
instead of using your password. It is more secure than using your password.

Follow the instructions on the official website to authenticate with a personal
access token (classic):
<https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry>.

> [!NOTE]
>
> You can find the personal access token in the settings of your GitHub account:
> **Settings** > **Developer settings** (at the very end of the left side bar) >
> **Personal access tokens** > **Tokens (classic)**.

### Login to GitHub Container Registry

Login to GitHub Container Registry with the following command, replacing
`<username>` with your GitHub username:

```bash
# Login to GitHub Container Registry
docker login ghcr.io -u <username>
```

When asked for the password, use the personal access token you created earlier.

### Tag the image correctly for GitHub Container Registry

```bash
# Tag the image with the correct format
docker tag purrchase ghcr.io/heigvd-s3-dai/purrchase:latest
```

You can delete the local `purrchase` image with the following command:

```bash
# Delete purrchase image
docker rmi purrchase
```

### Publish the image on GitHub Container Registry

```bash
# Publish the image on GitHub Container Registry
docker push ghcr.io/heigvd-s3-dai/purrchase
```

### Connect the package to the repository

> [!NOTE]
>
> This is only needed when pushing the image for the first time

Navigate to the organisation packages
(https://github.com/orgs/HEIGVD-S3-DAI/packages) and change the following:

- Change the visibility to public
- Connect to the `purrchase` repository

## Setup virtual machine

### Create a virtual machine

Return to the Azure portal and create a new virtual machine from the dashboard
in section `Create a resource`.

Select a virtual machine with the following characteristics:

- **Project details**
  - **Subscription**: Azure for Students
  - **Resource group**: Create new with the name `heig-vd-dai-course`
- **Instance details**
  - **Virtual machine name**: `heig-vd-dai-course-vm`
  - **Region**: (Europe) West Europe
  - **Availability options**: No infrastructure redundancy required
  - **Security type**: Trusted launch virtual machines (the default)
  - **Image**: Ubuntu Server 24.04 LTS - x64 Gen2 (the default)
  - **VM architecture**: x64
  - **Size**: `Standard_B1s` - you might need to click _"See all sizes"_ to see
    this option
- **Administrator account**
  - **Authentication type**: SSH public key
  - **Username**: `ubuntu` - please use this username so the teaching staff can
    help you if needed
  - **SSH public key source**: Use existing public key
  - **SSH public key**: Paste your public key here - see the note below for more
    information
- **Inbound port rules**
  - **Public inbound ports**: Allow selected ports
  - **Select inbound ports**: HTTP (80), HTTPS (443), SSH (22)

Although the `Standard_B1s` size is one of the
[cheapest](https://azure.microsoft.com/en-us/pricing/details/virtual-machines/linux/)
and least powerful option, it will be enough for this course. It will allow you
to use your remaining credits for other services.

Click on the `Review + create` button.

Validate the configuration and click on the `Create` button.

### Connect to the virtual machine

Connect through ssh with the following command:

```bash
ssh ubuntu@<vm public ip>
# or
ssh -i <path/to/private/key> ubuntu@<vm public ip>
```

### Update and secure the virtual machine

Once connected to the virtual machine, you can update the packages with the
following command:

```bash
# Update the available packages
sudo apt update

# Upgrade the packages
sudo apt upgrade
```

You can then reboot the virtual machine with the following command to apply all
the updates:

```bash
# Reboot the virtual machine
sudo reboot
```

### Install and configure Docker and Docker Compose

Go to the official website and follow the instructions to install
[Docker Engine](https://docs.docker.com/engine/) on your distribution **from the
repository** (not using Docker Desktop). In our case we are using
[Ubuntu with Docker](https://docs.docker.com/engine/install/ubuntu/).

Then, follow the post-installation steps to finalize the installation:
<https://docs.docker.com/engine/install/linux-postinstall/> (steps _"Manage
Docker as a non-root user"_ and _"Configure Docker to start on boot with
systemd"_).

##### Check the installation

Once Docker and Docker Compose are installed, you can check the installation by
running the following commands in a terminal:

```bash
# Check the Docker version
docker --version

# Check the Docker Compose version
docker compose version
```

The output should be similar to the following:

```text
Docker version 27.5.0, build a187fa5

Docker Compose version v2.32.4
```

### Install `apache2-utils`

In order to create passwords for Traefik, we will use the `htpasswd` utility.
Install it with the following commands:

```bash
sudo apt-get update
sudo apt-get install apache2-utils
```

## Deploy on the virtual machine

Start first by either cloning the repository or using `rsync` to copy the
project to the virtual machine. For `rsync` you can use the following command.

```bash
rsync -avz -e "ssh" . ubuntu@<vm public ip>:/home/ubuntu/purrchase
```

### Obtain a domain name

Access <http://www.duckdns.org/> and log in with your GitHub account.

Click on the `Add Domain` button and choose a domain name.

### Configure Traefik

For this part, navigate to the `traefik/` directory in the VM.

#### Create a user

First, configure the authorised users to the Traefik dashboard by following
these steps:

1. Create `secrets` directory:

   ```bash
   mkdir secrets
   ```

2. Create a new user

   ```bash
   htpasswd -c secrets/auth-users.txt <username>
   ```

   `htpasswd` will ask you to enter the password for the user.

#### Setup environments variables

Update the `.env` file with your own values:

- `TRAEFIK_ACME_EMAIL`: your email address
- `TRAEFIK_ROOT_FULLY_QUALIFIED_DOMAIN_NAME`: the root fully qualified domain
  name to access all your services - for example, if you want to access your
  services with the `https://whoami.my-domain-name.duckdns.org` URL, you must
  set `TRAEFIK_ROOT_FULLY_QUALIFIED_DOMAIN_NAME=my-domain-name.duckdns.org`

Update the `dns-challenge.env` file with the following environment variables:

- `DUCKDNS_TOKEN`: the token to access the Duck DNS API

### Start Traefik

Run Traefik with the following command:

```bash
sudo docker compose up -d
```

This will run the Traefik application in the background.

### Start the API

To start the API navigate to the `api/` directory in the VM and run the
following command:

```bash
sudo docker compose up -d
```

This will run the Javalin application in the background.

## Summary

Now you should be able to access:

- The API at: `https://<your domain>/api`
- The Traefik dashboard at: `https://<your domain>/traefik`. You can login with
  the credentials you created earlier.
