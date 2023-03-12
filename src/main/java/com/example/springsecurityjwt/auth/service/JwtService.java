package com.example.springsecurityjwt.auth.service;

import com.example.springsecurityjwt.auth.dto.LoginResponse;
import com.example.springsecurityjwt.auth.exception.TokenExpiredException;
import com.example.springsecurityjwt.auth.exception.TokenInvalidFormException;
import com.example.springsecurityjwt.auth.exception.TokenInvalidSecretKeyExeption;
import com.example.springsecurityjwt.common.error.exception.ErrorType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;

import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;

    private final long accessTokenExpireInMilliseconds;

    private final long refreshTokenExpireInMilliseconds;

    public JwtService(@Value("${jwt.secret-key}") String secretKey,
                      @Value("${jwt.access-token.expire-length}") long accessTokenExpireInMilliseconds,
                      @Value("${jwt.refresh-token.expire-length}") long refreshTokenExpireInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpireInMilliseconds = accessTokenExpireInMilliseconds;
        this.refreshTokenExpireInMilliseconds = refreshTokenExpireInMilliseconds;
    }

    public LoginResponse createTokenResponse(String email, String role) {
        String accessToken = createAccessToken(email, role);
        String refreshToken = createRefreshToken(email, role);
        return new LoginResponse(accessToken, refreshToken);
    }

    public Claims getBodyFromToken(String token) {
        return getClaims(token).getBody();
    }

    public void validateToken(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            validateExpiredToken(claims);
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenInvalidSecretKeyExeption(ErrorType.INVALID_SCRETKEY.getCode());
        }
    }

    public String createAccessToken(String eamil, String role) {
        return createToken(eamil, role, accessTokenExpireInMilliseconds);
    }

    public String createRefreshToken(String email, String role) {
        return createToken(email, role, refreshTokenExpireInMilliseconds);
    }

    private String createToken(String email, String role, long expireInMilliseconds) {
        Date iat = new Date(System.currentTimeMillis());
        Date exp = new Date(iat.getTime() + expireInMilliseconds);
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (IllegalArgumentException | MalformedJwtException e) {
            throw new TokenInvalidFormException(ErrorType.MAL_FORMED_TOKEN.getCode());
        } catch (SignatureException e) {
            throw new TokenInvalidSecretKeyExeption(ErrorType.INVALID_SCRETKEY.getCode());
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(ErrorType.EXPIERD_TOKEN.getCode());
        }
    }

    private void validateExpiredToken(Jws<Claims> claims) {
        Date exp = claims.getBody().getExpiration();
        if (exp.before(new Date())) {
            throw new TokenExpiredException(ErrorType.EXPIERD_TOKEN.getCode());
        }
    }

}
