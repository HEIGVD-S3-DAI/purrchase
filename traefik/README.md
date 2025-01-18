# Traefik

<!-- mtoc-start -->

* [Overview](#overview)
* [Environment Variables](#environment-variables)
  * [`.env`](#env)
  * [`dns-challenge.env`](#dns-challengeenv)
* [Configuration](#configuration)
  * [Entrypoints](#entrypoints)
    * [Web Configuration](#web-configuration)
    * [Websecure Configuration](#websecure-configuration)
  * [Certificates and DNS Challenge](#certificates-and-dns-challenge)
  * [Networking and Secrets](#networking-and-secrets)
  * [Volume Mounts](#volume-mounts)
  * [Access Control](#access-control)
* [Usage](#usage)

<!-- mtoc-end -->

## Overview

This summary provides a concise overview of the Traefik configuration, detailing
the routes, ports, and access controls.

## Environment Variables

### `.env`

- `TRAEFIK_IMAGE_VERSION`: The version of the Traefik image to use.
- `TRAEFIK_ROOT_FULLY_QUALIFIED_DOMAIN_NAME`: The root domain for which Traefik
  will handle certificates.
- `TRAEFIK_ACME_EMAIL`: The email address registered with Let's Encrypt for
  certificate generation.
- `TRAEFIK_ACME_DNS_PROVIDER`: The DNS provider to be used for ACME (Automated
  Certificate Management Environment) challenges.

### `dns-challenge.env`

- `DUCKDNS_TOKEN`: The token required for the provided DNS challenge to interact
  with the DNS service (in this case, DuckDNS).

## Configuration

### Entrypoints

#### Web Configuration

- **Port**: 80
- **Redirection**: All HTTP traffic to HTTPS (`websecure` entrypoint).

#### Websecure Configuration

- **Port**: 443
- **TLS/SSL**: Enabled
- **Certificate Resolver**: `letsencrypt`
- **Domains**:
  - Main Domain: `${TRAEFIK_ROOT_FULLY_QUALIFIED_DOMAIN_NAME}`
  - Wildcard Subdomains: `*.${TRAEFIK_ROOT_FULLY_QUALIFIED_DOMAIN_NAME}`

### Certificates and DNS Challenge

- **Email for Let's Encrypt**: `${TRAEFIK_ACME_EMAIL}`
- **Storage for ACME Certificates**: `/letsencrypt/acme.json`
- **DNS Challenge Provider**: `${TRAEFIK_ACME_DNS_PROVIDER}` (e.g., DuckDNS)
- **DNS Challenge Delay Before Check**: 30 seconds

### Networking and Secrets

- **Network**: `traefik_network`
- **Secrets**:
  - `auth_users`: Stores basic authentication users from
    `./secrets/auth-users.txt`

### Volume Mounts

- **Docker Socket**: `/var/run/docker.sock:/var/run/docker.sock:ro`
- **ACME Certificates**: `./letsencrypt:/letsencrypt:rw`

### Access Control

- **Basic Authentication**: Required for accessing the Traefik dashboard. Users
  are defined in `./secrets/auth-users.txt`.

## Usage

See [deploy.md](../docs/deploy.md) documentation for instructions for the
deployment
