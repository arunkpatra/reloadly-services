package com.reloadly.auth.jwt;

import com.reloadly.auth.config.AuthenticationServiceProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * A JWT token utility providing various functions.
 *
 * @author Arun Patra
 */
public class JwtTokenUtil implements Serializable {

    private final AuthenticationServiceProperties properties;

    public JwtTokenUtil(AuthenticationServiceProperties properties) {
        this.properties = properties;
    }

    //retrieve username from jwt token
    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getIssuerFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuer);
    }

    public String getAudienceFromToken(String token) {
        return getClaimFromToken(token, Claims::getAudience);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(properties.getJwtSecretKey().getBytes())).build()
                .parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
//    public String generateToken(String subject) {
//        Map<String, Object> claims = new HashMap<>();
//        return doGenerateToken(claims, subject);
//    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return doGenerateToken(claims, subject);
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + properties.getJwtTokenValiditySeconds() * 1000))
                .signWith(Keys.hmacShaKeyFor(properties.getJwtSecretKey().getBytes())).compact();
    }

    //validate token
    public Boolean validateToken(String token) {
        // We check the following
        // 1. Subject
        // 2. Issuer
        // 3. Audience
        // 4. Token Expiry
        return ((null != getSubjectFromToken(token)) &&
                properties.getAudience().equals(getAudienceFromToken(token)) &&
                properties.getIssuer().equals(getIssuerFromToken(token)) &&
                !isTokenExpired(token));
    }
}

