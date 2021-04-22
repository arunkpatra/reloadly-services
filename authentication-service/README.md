# Authentication Service

This microservice provides authentication related services. 

## Capabilities

- `/login`: Securely perform username/password based authentication. This is the only supported authentication
  method. Implementing phone number/One Time Password(OTP) or Social Login are other possibilities. All
  such methods would be mere extensions to the current infrastructure.
- `/signup` Signup new users based on username/password. This is a self signup process.

## API

### 1. Authentication

#### Description
Authenticates a user and returns a JWT token. The response includes token issue and expiry timestamps. 
Clients should consider checking these fields before using the token for accessing APIs secured by
this Authentication Service. 

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

- `401` : Unauthorized. The authentication attempt has failed. The response will contain an empty token.
- `200` : Authentication was successful. The response will contain a valid JWT token along with issue and expiry timestamps