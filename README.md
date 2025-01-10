# Purrchase

## About

TODO

### Purpose

TODO

## Usage

Make sure you have docker installed on your machine. You can refer to the
[Development](#development) section on how to run the application without
docker.

To run the aplication, you can use the following commands:

```bash
# Pull the image
docker pull ghcr.io/heigvd-s3-dai/purrchase:latest

# Run the application
docker run --network host --rm -it ghcr.io/heigvd-s3-dai/purrchase:latest -h
```

You should obtain the following output.

```bash
Usage: app.jar [-hV] [COMMAND]
A typing game client-server application
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  client  Start a client to connect to the server
  server  Start a server to connect to the server
```

### Server

To see the available commands for server, run the following command.

```bash
docker run --network host --rm -it ghcr.io/heigvd-s3-dai/purrchase:latest server -h
```

You should obtain the following output.

```bash
// TODO: Help
```

## Example

The following will demonstrate how to run the application locally.

1. Start the server:
   ```bash
   docker run --network host --rm -it ghcr.io/heigvd-s3-dai/purrchase:latest server
   ```
2. Start the client:
   ```bash
   docker run --network host --rm -it ghcr.io/heigvd-s3-dai/purrchase:latest client -I eth0
   ```
3. The client will prompt you to enter your username. Enter your username and
   press enter.
4. Repeat step 2 and 3 in a new terminal to register a second client.
5. When you're ready, press enter on each client.
6. When everyone is ready, the game starts. Good luck !

Here's how the waiting screen looks like.

![Lobby Screenshot](docs/lobby.png)

Here's how it looks like during the game.

![In game Screenshot](docs/ingame.png)

Here's how it looks like when the game is finished.

![End game Screenshot](docs/endgame.png)

## Development

Start by cloning the repository:

```bash
git clone https://github.com/HEIGVD-S3-DAI/purrchase.git
```

Make sure you make java jdk>=21 installed on your machine and follow the steps
below:

```bash
# Download the dependencies and their transitive dependencies
./mvnw dependency:go-offline
```

```bash
# Package the application
./mvnw package
```

This will create a jar file in the `target/` folder.

Optionally, create an alias to the jar application with the command below:

```bash
alias purrchase="java -jar target/java-purrchase-1.0-SNAPSHOT.jar"
```

To see a list of avaiable commands, run:

```bash
purrchase -h
```

### Building the Docker Image

To build the docker image, run the following command:

```bash
docker build -t ghcr.io/heigvd-s3-dai/purrchase:latest .
```

### Pushing the Docker Image

To push the docker image, make sure you have logged in to the registry and run
the following command:

```bash
docker push ghcr.io/heigvd-s3-dai/purrchase:latest
```

## References

TODO

## Authors

- Leonard Cseres [@leoanrdcser](https://github.com/leonardcser)
- Aude Laydu [@eau2](https://github.com/eau2)
