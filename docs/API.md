# API

- [Users API](#users-api)
  - [Endpoints](#endpoints)
    - [Create a new user](#create-a-new-user)
    - [Get many users](#get-many-users)
    - [Get one user](#get-one-user)
    - [Update a user](#update-a-user)
    - [Delete a user](#delete-a-user)
    - [Login](#login)
    - [Logout](#logout)
    - [Profile](#profile)
- [Cats API](#cats-api)
  - [Endpoints](#endpoints-1)

The api is split into to the [Users API](#users-api) and the
[Cats API](#cats-api). It uses the HTTP protocol on port `8080`.

The JSON format is used to exchange data. The `Content-Type` header must be set
to `application/json` when sending data to the API. The `Accept` header must be
set to `application/json` when receiving data from the API.

## Users API

The users API allows to manage users.

The API is based on the CRUD pattern. It has the following operations:

- Create a new user
- Get many users that you can filter by first name and/or last name
- Get one user by its ID
- Update a user
- Delete a user

Users are also able to login and logout. They can also access their profile to
validate their information using a cookie.

### Endpoints

#### Create a new user

- `POST /api/users`

Create a new user.

##### Request

The request body must contain a JSON object with the following properties:

- `firstName` - The first name of the user
- `lastName` - The last name of the user
- `email` - The email address of the user
- `password` - The password of the user

##### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the user
- `firstName` - The first name of the user
- `lastName` - The last name of the user
- `email` - The email address of the user
- `password` - The password of the user

##### Status codes

- `201` (Created) - The user has been successfully created
- `400` (Bad Request) - The request body is invalid
- `409` (Conflict) - The user already exists

#### Get many users

- `GET /api/users`

Get many users.

##### Request

The request can contain the following query parameters:

- `firstName` - The first name of the user
- `lastName` - The last name of the user

##### Response

The response body contains a JSON array with the following properties:

- `id` - The unique identifier of the user
- `firstName` - The first name of the user
- `lastName` - The last name of the user
- `email` - The email address of the user
- `password` - The password of the user

##### Status codes

- `200` (OK) - The users have been successfully retrieved
- `304` (Not modified) - The users are already in the cache

#### Get one user

- `GET /api/users/{id}`

Get one user by its ID.

##### Request

The request path must contain the ID of the user.

##### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the user
- `firstName` - The first name of the user
- `lastName` - The last name of the user
- `email` - The email address of the user
- `password` - The password of the user

##### Status codes

- `200` (OK) - The user has been successfully retrieved
- `304` (Not modified) - The user is already in the cache
- `404` (Not Found) - The user does not exist

#### Update a user

- `PUT /api/users`

Update a user by its ID, getting it from the user token.

##### Request

The request path must contain the ID of the user.

The request body must contain a JSON object with the following properties:

- `firstName` - The first name of the user
- `lastName` - The last name of the user
- `email` - The email address of the user
- `password` - The password of the user

##### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the user
- `firstName` - The first name of the user
- `lastName` - The last name of the user
- `email` - The email address of the user
- `password` - The password of the user

##### Status codes

- `200` (OK) - The user has been successfully updated
- `400` (Bad Request) - The request body is invalid
- `404` (Not Found) - The user does not exist
- `412` (Precondition Failed) - The user in the cache isn't up to date

#### Delete a user

- `DELETE /api/users`

Delete a user by its ID, getting it from the user token.

##### Request

The request path must contain the ID of the user.

##### Response

The response body is empty.

##### Status codes

- `204` (No Content) - The user has been successfully deleted
- `404` (Not Found) - The user does not exist
- `412` (Precondition Failed) - The user in the cache isn't up to date

#### Login

- `POST /login`

Login a user.

##### Request

The request body must contain a JSON object with the following properties:

- `email` - The email address of the user
- `password` - The password of the user

##### Response

The response body is empty. A `user` cookie is set with the ID of the user.

##### Status codes

- `204` (No Content) - The user has been successfully logged in
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - The user does not exist or the password is incorrect

#### Logout

- `POST /logout`

Logout a user.

##### Request

The request body is empty.

##### Response

The response body is empty. The `user` cookie is removed.

##### Status codes

- `204` (No Content) - The user has been successfully logged out

#### Profile

- `GET /profile`

Get the current user (the user that is logged in).

##### Request

The request body is empty.

##### Response

The response body contains a JSON object with the following properties:

- `id` - The unique identifier of the user
- `firstName` - The first name of the user
- `lastName` - The last name of the user
- `email` - The email address of the user
- `password` - The password of the user

##### Status codes

- `200` (OK) - The user has been successfully retrieved
- `401` (Unauthorized) - The user is not logged in

## Cats API

Once the user is logged in, the cat API allows to manage cats.

The API is based on the CRUD pattern. It has the following operations:

- Create a new cat
- Get many cats that you can filter by breed, color, age and userId (each of
  these criteria is optional)
- Get one cat by its ID
- Update a cat
- Delete a cat

### Endpoints

#### Create a cat

- `POST /api/cats`

Create a new cat.

##### Request

The request body contains a JSON object with the following properties:

- `name`- The name of the cat
- `breed` - The breed of the cat
- `age` - The age of the cat
- `color` - The color of the cat
- `imageURL` - The URL corresponding to a picture of the cat

##### Response

The response body contains a JSON object with the following properties:

- `id` - The id attributed to the cat
- `name`- The name of the cat
- `breed` - The breed of the cat
- `age` - The age of the cat
- `color` - The color of the cat
- `imageURL` - The URL corresponding to a picture of the cat
- `userId` - The userId of the user that added the cat

##### Status codes

- `201` (Created) - The cat has been successfully created
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - The user is not logged in

#### Get many cats

- `GET /api/cats`

Get many cats.

##### Request

The request can contain the following optionals query parameters:

- `breed` - The breed of the cat
- `age` - The age of the cat
- `color` - The color of the cat
- `userId` - The userId of the user that added the cat

##### Response

The response body contains a JSON array with the following properties:

- `id` - The id attributed to the cat
- `name`- The name of the cat
- `breed` - The breed of the cat
- `age` - The age of the cat
- `color` - The color of the cat
- `imageURL` - The URL corresponding to a picture of the cat
- `userId` - The userId of the user that added the cat

##### Status code

- `200` (OK) - The cats have been successfully retrieved
- `304` (Not modified) - The cats are already in the cache
- `401` (Unauthorized) - The user is not logged in
- `404` (Not Found) - There is no cat corresponding to the selected filters

#### Get one cat

- `GET /api/cats/{id}`

Get one cat by its ID.

##### Request

The request path must contain the ID of the cat.

##### Response

The response body contains a JSON object with the following properties:

- `id` - The id attributed to the cat
- `name`- The name of the cat
- `breed` - The breed of the cat
- `age` - The age of the cat
- `color` - The color of the cat
- `imageURL` - The URL corresponding to a picture of the cat
- `userId` - The userId of the user that added the cat

##### Status code

- `200` (OK) - The cat has been successfully retrieved
- `304` (Not modified) - The cat is already in the cache
- `401` (Unauthorized) - The user is not logged in
- `404` (Not Found) - The cat does not exist

#### Update a cat

- `PUT /api/cats/{id}`

Update a cat by its ID.

##### Request

The request path must contain the ID of the cat.

The request body must contain a JSON object with the following properties:

- `name`- The name of the cat
- `breed` - The breed of the cat
- `age` - The age of the cat
- `color` - The color of the cat
- `imageURL` - The URL corresponding to a picture of the cat

##### Response

The response body contains a JSON object with the following properties:

- `id` - The id attributed to the cat
- `name`- The name of the cat
- `breed` - The breed of the cat
- `age` - The age of the cat
- `color` - The color of the cat
- `imageURL` - The URL corresponding to a picture of the cat
- `userId` - The userId of the user that added the cat

##### Status code

- `200` (OK) - The cat has been successfully updated
- `400` (Bad Request) - The request body is invalid
- `401` (Unauthorized) - The user is not logged in
- `403` (Forbidden) - The user is not allowed
- `404` (Not Found) - The cat does not exist
- `412` (Precondition Failed) - The cat in the cache isn't up to date

#### Delete a cat

- `DELETE /api/cats/{id}`

Delete a cat by its ID.

##### Request

The request path must contain the ID of the cat.

##### Response

The response body is empty.

##### Status code

- `204` (No Content) - The user had been successfully deleted
- `401` (Unauthorized) - The user is not logged in
- `403` (Forbidden) - The user is not allowed
- `404` (Not Found) - The cat does not exist
- `412` (Precondition Failed) - The cat in the cache isn't up to date

