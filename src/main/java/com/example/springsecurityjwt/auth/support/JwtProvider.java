package com.example.springsecurityjwt.auth.support;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey key;

    private final long accessTokenExpireInMilliseconds;

    private final long refreshTokenExpireInMilliseconds;

    public JwtProvider(@Value("${security.jwt.secret-key}") String secretKey,
                       @Value("${security.jwt.access-token-expire-length}") long accessTokenExpireInMilliseconds,
                       @Value("${security.jwt.refresh-token-expire-length}") long refreshTokenExpireInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpireInMilliseconds = accessTokenExpireInMilliseconds;
        this.refreshTokenExpireInMilliseconds = refreshTokenExpireInMilliseconds;
    }

    public String createAccessToken(Long id, String email, String role) {
        return createToken(id, email, role, accessTokenExpireInMilliseconds);
    }

    public String createRefreshToken(Long id, String email, String role) {
        return createToken(id, email, role, refreshTokenExpireInMilliseconds);
    }

    private String createToken(Long id, String email, String role, long expireInMilliseconds) {
        Date iat = new Date(System.currentTimeMillis());
        Date exp = new Date(iat.getTime() + expireInMilliseconds);
        return Jwts.builder()
                .setSubject(Long.toString(id))
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getPayload(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }




}
