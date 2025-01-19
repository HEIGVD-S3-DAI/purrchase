# Purrchase

## About

Purrchase is an API allowing users to share their cats with each other.
To learn more about the different endpoints, refer to the [API documentation](./docs/API.md).
It is deployed to https://purrchase.duckdns.org/api, and you can access the treaefik dashboard to the following url : https://purrchase.duckdns.org/traefik, using
`user: admin` and `password: admin`.

### Purpose

The purpose of this project is to create a simple API with CRUD operations,
authentication, session management and cache using Javalin and the HTTP protocol.

## Usage

Make sure you have docker installed on your machine. You can refer to the
[Development](#development) section on how to run the application without
docker.

To run the application with docker, you can use the following commands:

```bash
# Pull the image
docker pull ghcr.io/heigvd-s3-dai/purrchase:latest

# Run the application
docker run --network host --rm -it ghcr.io/heigvd-s3-dai/purrchase:latest -h
```

You should obtain the following output.

```bash
11:26:38.567 [main] INFO  io.javalin.Javalin - Starting Javalin ...
11:26:38.574 [main] INFO  org.eclipse.jetty.server.Server - jetty-11.0.23; built: 2024-08-14T01:40:17.906Z; git: 6fcf5ccaebd7ca13a0cb96c96adca699a24080a0; jvm 21.0.5+11-LTS
11:26:38.710 [main] INFO  o.e.j.s.s.DefaultSessionIdManager - Session workerName=node0
11:26:38.732 [main] INFO  o.e.j.server.handler.ContextHandler - Started o.e.j.s.ServletContextHandler@6736fa8d{/,null,AVAILABLE}
11:26:38.748 [main] INFO  o.e.jetty.server.AbstractConnector - Started ServerConnector@957e06{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
11:26:38.792 [main] INFO  org.eclipse.jetty.server.Server - Started Server@55a147cc{STARTING}[11.0.23,sto=0] @1483ms
11:26:38.792 [main] INFO  io.javalin.Javalin -
       __                  ___           _____
      / /___ __   ______ _/ (_)___      / ___/
 __  / / __ `/ | / / __ `/ / / __ \    / __ \
/ /_/ / /_/ /| |/ / /_/ / / / / / /   / /_/ /
\____/\__,_/ |___/\__,_/_/_/_/ /_/    \____/

       https://javalin.io/documentation

11:26:38.794 [main] INFO  io.javalin.Javalin - Javalin started in 448ms \o/
11:26:38.800 [main] INFO  io.javalin.Javalin - Listening on http://localhost:8080/
11:26:38.838 [main] INFO  io.javalin.Javalin - You are running Javalin 6.3.0 (released August 22, 2024. Your Javalin version is 149 days old. Consider checking for a newer version.).
```

To run the application with docker compose, you can go to the api directory and run the following command.

```bash
docker compose up --build
```

You should obtain the following output.

```bash
[+] Running 1/1
 ✔ Container purrchase-api  Created                                                                                                                                                      0.1s 
Attaching to purrchase-api
purrchase-api  | 11:57:43.307 [main] INFO  io.javalin.Javalin - Starting Javalin ...
purrchase-api  | 11:57:43.314 [main] INFO  org.eclipse.jetty.server.Server - jetty-11.0.23; built: 2024-08-14T01:40:17.906Z; git: 6fcf5ccaebd7ca13a0cb96c96adca699a24080a0; jvm 21.0.5+11-LTS 
purrchase-api  | 11:57:43.455 [main] INFO  o.e.j.s.s.DefaultSessionIdManager - Session workerName=node0
purrchase-api  | 11:57:43.479 [main] INFO  o.e.j.server.handler.ContextHandler - Started o.e.j.s.ServletContextHandler@6736fa8d{/,null,AVAILABLE}
purrchase-api  | 11:57:43.494 [main] INFO  o.e.jetty.server.AbstractConnector - Started ServerConnector@957e06{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
purrchase-api  | 11:57:43.535 [main] INFO  org.eclipse.jetty.server.Server - Started Server@55a147cc{STARTING}[11.0.23,sto=0] @1373ms
purrchase-api  | 11:57:43.536 [main] INFO  io.javalin.Javalin -
purrchase-api  |        __                  ___           _____
purrchase-api  |       / /___ __   ______ _/ (_)___      / ___/
purrchase-api  |  __  / / __ `/ | / / __ `/ / / __ \    / __ \
purrchase-api  | / /_/ / /_/ /| |/ / /_/ / / / / / /   / /_/ /
purrchase-api  | \____/\__,_/ |___/\__,_/_/_/_/ /_/    \____/
purrchase-api  |
purrchase-api  |        https://javalin.io/documentation
purrchase-api  |
purrchase-api  | 11:57:43.543 [main] INFO  io.javalin.Javalin - Javalin started in 458ms \o/
purrchase-api  | 11:57:43.553 [main] INFO  io.javalin.Javalin - Listening on http://localhost:8080/
purrchase-api  | 11:57:43.583 [main] INFO  io.javalin.Javalin - You are running Javalin 6.3.0 (released August 22, 2024. Your Javalin version is 149 days old. Consider checking for a newer version.).
```

To stop the container run the following command.

```bash
docker compose down
```

You should obtain the following output.

```bash
[+] Running 1/0
 ✔ Container purrchase-api  Removed  
```



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

You can then run it using the following command.

```bash
java -jar target/java-purrchase-1.0-SNAPSHOT.jar
```

To deploy the project refer to the [Deployment documentation](./docs/deploy.md).

## Example

To use the API we will be using curl. To install curl, run the following command.
```bash
# Install curl
sudo apt install curl
```

We deployed the api to the following url : https://purrchase.duckdns.org/api,
it will be used in the examples curl command below,
if you're deploying the project yourself change it to your own url.

#### Create a user

First we need to create a user.

```bash
curl -i -X POST "https://purrchase.duckdns.org/api/users" \
  -H "Content-Type: application/json" \
  -d '{
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@gmail.com",
        "password": "Password"
      }'
```

You should obtain the following output.

```angular2html
HTTP/2 201
content-type: application/json
date: Sun, 19 Jan 2025 15:34:30 GMT
etag: b70dde62
set-cookie: userSession=823aac97009daea; Path=/; HttpOnly
content-length: 95

{"id":1,"firstName":"John","lastName":"Doe","email":"john.doe@gmail.com","password":"Password"}
```

You will need to get the userSession cookie it gives you and use it in the following requests.

#### Login

Login with the email and password you previously set.

```bash
curl -i -X POST "https://purrchase.duckdns.org/api/login" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea;" \
  -d '{
        "email": "john.doe@gmail.com",
        "password": "Password"
      }'
```

You should obtain the following output.

```angular2html
HTTP/2 204
content-type: text/plain
date: Sun, 19 Jan 2025 15:35:33 GMT
expires: Thu, 01 Jan 1970 00:00:00 GMT
set-cookie: user=1; Path=/
```

You will need to get the user cookie it gives you and use it in the following requests.

#### Profile

You can access your profile to check you are correctly logged in or see your informations.

```bash
curl -X GET "https://purrchase.duckdns.org/api/profile" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;"
```

You should obtain the following output.

```angular2html
HTTP/2 200
content-type: application/json
date: Sun, 19 Jan 2025 15:36:31 GMT
etag: b70dde62
content-length: 95

{"id":1,"firstName":"John","lastName":"Doe","email":"john.doe@gmail.com","password":"Password"}
```

#### Add cats

Let's add one cat.

```bash
curl -i -X POST "https://purrchase.duckdns.org/api/cats" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -d '{
        "name": "Whiskers", 
        "breed": "Siamese", 
        "age": 3, 
        "color": "Gray", 
        "imageURL": "http://example.com/whiskers.jpg"
      }'
```

You should obtain the following output.

```angular2html
HTTP/2 201
content-type: application/json
date: Sun, 19 Jan 2025 12:49:34 GMT
etag: b43ee254
content-length: 123

{"id":1,"name":"Whiskers","breed":"Siamese","age":3,"color":"Gray","imageURL":"http://example.com/whiskers.jpg","userId":1}
```

Add a few more cat so it is interesting to filter them. For each of them the output should be similar to the first cat.

```bash 
curl -i -X POST "https://purrchase.duckdns.org/api/cats" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -d '{
        "name": "Smokey", 
        "breed": "Maine Coon", 
        "age": 4, 
        "color": "Gray", 
        "imageURL": "http://example.com/smokey.jpg"
      }'

curl -i -X POST "https://purrchase.duckdns.org/api/cats" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -d '{
        "name": "Shadow", 
        "breed": "Maine Coon", 
        "age": 4, 
        "color": "Black", 
        "imageURL": "http://example.com/shadow.jpg"
      }'
```

#### Get one cat by ID

To get information for one cat use the following request.
Here we use cats/1, but you can replace the 1 with the ID of the cat you want.

```bash
curl -i -X GET "https://purrchase.duckdns.org/api/cats/1" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;"
```

You should obtain the following output.

```angular2html
HTTP/2 200
content-type: application/json
date: Sun, 19 Jan 2025 12:54:01 GMT
etag: b43ee254
set-cookie: userSession=823aac97009daea; Path=/; HttpOnly
content-length: 123

{"id":1,"name":"Whiskers","breed":"Siamese","age":3,"color":"Gray","imageURL":"http://example.com/whiskers.jpg","userId":1}
```

Now we have an etag for requesting cat with ID = 1. If we want to request it again we can use.

```bash
curl -i -X GET "https://purrchase.duckdns.org/api/cats/1" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -H "If-None-Match: b43ee254"
```

You should obtain the following output.

```angular2html
HTTP/2 304
content-type: application/json
date: Sun, 19 Jan 2025 12:57:20 GMT
```

HTTP code `304` indicate that the data wasn't modified.

#### Get many cats

To get many cats with different filters use the following curl requests.
If no filter is specified you will get all cats. Else you can filter by color, breed, age and userId.

##### Get all gray cats
```bash
curl -i -X GET "https://purrchase.duckdns.org/api/cats?color=Gray" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;"
```

You should obtain the following output.

```angular2html
HTTP/2 200
content-type: application/json
date: Sun, 19 Jan 2025 13:01:26 GMT
etag: 1289772d
content-length: 248

[{"id":1,"name":"Whiskers","breed":"Siamese","age":3,"color":"Gray","imageURL":"http://example.com/whiskers.jpg","userId":1},
{"id":2,"name":"Smokey","breed":"Maine Coon","age":4,"color":"Gray","imageURL":"http://example.com/smokey.jpg","userId":1}]
```

##### Get all cats that are maine coon

```bash
curl -i -X GET "https://purrchase.duckdns.org/api/cats?breed=Maine%20Coon" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;"
```

You should obtain the following output.

```angular2html
HTTP/2 200
content-type: application/json
date: Sun, 19 Jan 2025 13:03:34 GMT
etag: 1289772d
content-length: 248

[{"id":2,"name":"Smokey","breed":"Maine Coon","age":4,"color":"Gray","imageURL":"http://example.com/smokey.jpg","userId":1},
{"id":3,"name":"Shadow","breed":"Maine Coon","age":4,"color":"Black","imageURL":"http://example.com/shadow.jpg","userId":1}]
```

##### Get all cats that are gray and 4 years old

```bash
    curl -i -X GET "https://purrchase.duckdns.org/api/cats?color=Gray&age=4" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;"
```

You should obtain the following output.

```angular2html
HTTP/2 200
content-type: application/json
date: Sun, 19 Jan 2025 13:08:34 GMT
etag: 1289772d
content-length: 124

[{"id":2,"name":"Smokey","breed":"Maine Coon","age":4,"color":"Gray","imageURL":"http://example.com/smokey.jpg","userId":1}]
```

##### Update a cat
Let's change the color of the first cat.

```bash
    curl -i -X PUT "https://purrchase.duckdns.org/api/cats/1" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -H "If-Match: b43ee254" \
  -d '{
        "name": "Whiskers", 
        "breed": "Siamese", 
        "age": 3, 
        "color": "White", 
        "imageURL": "http://example.com/whiskers.jpg"
      }'
```
NOT WORKING ???????

You should obtain the following output.

```angular2html
todo
```

Now that the cat is modified, if you try to get if again with.

```bash 
curl -i -X GET "https://purrchase.duckdns.org/api/cats/1" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -H "If-None-Match: b43ee254"
```

You should obtain the following output.

```angular2html
todo
```

##### Delete a cat

To remove a cat, here the one with ID = 1, use.

```bash
curl -i -X DELETE https://purrchase.duckdns.org/api/cats/1 \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -H "If-Match: b43ee254"
```

NOT WORKING ???????

You should obtain the following output.

```angular2html
todo
```
##### Logout

You can log out using the following request.

```bash
curl -i -X POST https://purrchase.duckdns.org/api/logout \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;"
```

You should obtain the following output.

```angular2html
HTTP/2 204
content-type: text/plain
date: Sun, 19 Jan 2025 13:24:53 GMT
expires: Thu, 01 Jan 1970 00:00:00 GMT
set-cookie: user=; Path=/; Expires=Thu, 01-Jan-1970 00:00:00 GMT; Max-Age=0
```

##### Delete a user

You can delete a user using the following request.

```bash
curl -i -X DELETE https://purrchase.duckdns.org/api/users/2 \
  -H "Accept: application/json"
```

??? NOT WORKING

You should obtain the following output.

```angular2html
todo
```
## Authors

- Leonard Cseres [@leoanrdcser](https://github.com/leonardcser)
- Aude Laydu [@eau2](https://github.com/eau2)
