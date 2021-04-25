# Authentication Service

This microservice provides authentication related services.

## Capabilities

- `/login` - Securely perform username/password based authentication. This is the only supported authentication method.
  Implementing phone number/One Time Password(OTP) or Social Login are other possibilities. All such methods would be
  mere extensions to the current infrastructure.
- `/signup` - Signup new users based on username/password. This is a self signup process.
- `/verify/token` - Verify a Reloadly Authentication Service issued JWT token

## API

### 1. Authentication

#### Description

Authenticates a user and returns a JWT token. The response includes token issue and expiry timestamps. Clients should
consider checking these fields before using the token for accessing APIs secured by this Authentication Service.

The issues JWT token contains a unique Subject ID and a certain number of claims. While **_Authorization_** is best
decoupled from an authentication system, for simple use cases, the roles granted to a subject can be returned as claims
in the JWT token.

#### Endpoint

`/login`

#### HTTP Method

`POST`

#### Request

``` json
{
    "username" : "username",
    "password" : "password"
}
```

#### Response

``` json
{
    "token" : "the_issued_jwt_token_if_authentication_succeeds",
    "tokenIssuedAt" : "token_issue_timestamp",
    "tokenExpiresOn" : "token_expiry_timestamp",
}
```

#### Status Codes

| Status Code | Description |
|-------------|-------------|
| `200` | Authentication was successful. The response will contain a valid JWT token along with issue and expiry timestamps.|
| `401` | Unauthorized. The authentication attempt has failed. The response will contain an empty token.|
| `500` | Internal server error.|

### 2. Signup

#### Description

Allows a user to signup using a username and password. A unique UID is created in the system for the user.

#### Endpoint

`/signup`

#### HTTP Method

`POST`

#### Request

``` json
{
    "username" : "username",
    "password" : "password"
}
```

#### Response

``` json
{
    "uid" : "the UID for the newly created user",
    "message" : "an informational signup message"
}
```

#### Status Codes

| Status Code | Description |
|-------------|-------------|
| `201` | User created, signup successful.|
| `400` | Bad request.|
| `500` | Internal server error.|

### 3. Token verification

#### Description

Allows a Reloadly Authentication Service issued JWT token to be verified. If verification is successful, returns a a
verified and decoded token back.

#### Endpoint

`/verify/token`

#### HTTP Method

`POST`

#### Request

``` json
{
    "token" : "A Reloadly Auth Service issued JWT token"
}
```

#### Response

``` json
{
    "uid" : "The uid",
    "issuer" : "The issuer",
    "audience" : "The audience",
    "claims" : "The decoded claims for the token if verification succeeds",
}
```

> **NOTE**: The `claims` contain other details of interest like `iat` and `exp` which may be used by the caller.

#### Status Codes

| Status Code | Description |
|-------------|-------------|
| `200` | Token verification successful.|
| `401` | Token verification failed.|
| `500` | Internal server error.|
