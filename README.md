# Purrchase

## About

Purrchase is an API allowing users to share their cats with each other.
To learn more about the different endpoints, refer to the [API documentation](./docs/API.md).

### Purpose

The purpose of this project is to create a simple API with CRUD operations,
authentication, session management and cache using Javalin and the HTTP protocol.

## Usage

We deployed the api to the following url : https://purrchase.duckdns.org/api 

You can access the treaefik dashboard to the following url : https://purrchase.duckdns.org/traefik, using
`user: admin` and `password: admin`.

If you want to run it locally refer to [the development section](#Development)
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
curl -i -X GET "https://purrchase.duckdns.org/api/profile" \
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
date: Sun, 19 Jan 2025 17:05:22 GMT
etag: 87d7cf57
content-length: 123

{"id":5,"name":"Whiskers","breed":"Siamese","age":3,"color":"Gray","imageURL":"http://example.com/whiskers.jpg","userId":1}
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
Here we use cats/5, but you can replace the 5 with the ID of the cat you want.

```bash
curl -i -X GET "https://purrchase.duckdns.org/api/cats/5" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
```

You should obtain the following output.

```angular2html
HTTP/2 200
content-type: application/json
date: Sun, 19 Jan 2025 17:07:01 GMT
etag: 87d7cf57
content-length: 123

{"id":5,"name":"Whiskers","breed":"Siamese","age":3,"color":"Gray","imageURL":"http://example.com/whiskers.jpg","userId":1}
```

Now we have an etag for requesting cat with ID = 1. If we want to request it again we can use.

```bash
curl -i -X GET "https://purrchase.duckdns.org/api/cats/5" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -H "If-None-Match: 87d7cf57"
```

You should obtain the following output.

```angular2html
HTTP/2 304
content-type: application/json
date: Sun, 19 Jan 2025 17:08:31 GMT
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
date: Sun, 19 Jan 2025 17:09:19 GMT
etag: 8d543e3e
content-length: 248

[{"id":5,"name":"Whiskers","breed":"Siamese","age":3,"color":"Gray","imageURL":"http://example.com/whiskers.jpg","userId":1},
{"id":6,"name":"Smokey","breed":"Maine Coon","age":4,"color":"Gray","imageURL":"http://example.com/smokey.jpg","userId":1}]
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
date: Sun, 19 Jan 2025 17:09:49 GMT
etag: 8d543e3e
content-length: 248

[{"id":6,"name":"Smokey","breed":"Maine Coon","age":4,"color":"Gray","imageURL":"http://example.com/smokey.jpg","userId":1},
{"id":7,"name":"Shadow","breed":"Maine Coon","age":4,"color":"Black","imageURL":"http://example.com/shadow.jpg","userId":1}]
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
date: Sun, 19 Jan 2025 17:10:14 GMT
etag: 8d543e3e
content-length: 124

[{"id":6,"name":"Smokey","breed":"Maine Coon","age":4,"color":"Gray","imageURL":"http://example.com/smokey.jpg","userId":1}]
```

##### Update a cat
Let's change the color of the cat with ID = 5.

```bash
    curl -i -X PUT "https://purrchase.duckdns.org/api/cats/5" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -H "If-Match: 87d7cf57" \
  -d '{
        "name": "Whiskers", 
        "breed": "Siamese", 
        "age": 3, 
        "color": "White", 
        "imageURL": "http://example.com/whiskers.jpg"
      }'
```

You should obtain the following output.

```angular2html
HTTP/2 200
content-type: application/json
date: Sun, 19 Jan 2025 17:11:20 GMT
etag: b9ef635d
content-length: 124

{"id":5,"name":"Whiskers","breed":"Siamese","age":3,"color":"White","imageURL":"http://example.com/whiskers.jpg","userId":1}
```

Now that the cat is modified, if you try to get it again with the old etag `87d7cf57`.

```bash 
curl -i -X GET "https://purrchase.duckdns.org/api/cats/5" \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -H "If-None-Match: 87d7cf57"
```

You should obtain the following output.

```angular2html
HTTP/2 200
content-type: application/json
date: Sun, 19 Jan 2025 17:12:05 GMT
etag: b9ef635d
content-length: 124

{"id":5,"name":"Whiskers","breed":"Siamese","age":3,"color":"White","imageURL":"http://example.com/whiskers.jpg","userId":1}
```

Not receiving `304` but receiving `200` with the new data indicates that the data has changed since stored in cache.

##### Delete a cat

To remove a cat, here the one with ID = 5, use.

```bash
curl -i -X DELETE https://purrchase.duckdns.org/api/cats/5 \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -H "If-Match: b9ef635d"
```

You should obtain the following output.

```angular2html
HTTP/2 204
content-type: text/plain
date: Sun, 19 Jan 2025 17:14:09 GMT
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
date: Sun, 19 Jan 2025 17:14:34 GMT
expires: Thu, 01 Jan 1970 00:00:00 GMT
set-cookie: user=; Path=/; Expires=Thu, 01-Jan-1970 00:00:00 GMT; Max-Age=0
```

##### Delete a user

You need to be logged in to delete your account, so if you logged out, request this.

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

You can then delete your account using the following request.

```bash
curl -i -X DELETE https://purrchase.duckdns.org/api/users \
  -H "Accept: application/json" \
  -H "cookie: userSession=823aac97009daea; user=1;" \
  -H "If-Match: b70dde62"
```

You should obtain the following output.

```angular2html
HTTP/2 204
content-type: text/plain
date: Sun, 19 Jan 2025 17:18:08 GMT
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


## Authors

- Leonard Cseres [@leoanrdcser](https://github.com/leonardcser)
- Aude Laydu [@eau2](https://github.com/eau2)
