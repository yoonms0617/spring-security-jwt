package com.example.springsecurityjwt.auth.support;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @DisplayName("Access Token을 생성한다.")
    @Test
    void createAccessToken() {
        String accessToken = jwtProvider.createAccessToken(1L, "yoon@test.com", "ROLE_MEMBER");

        assertThat(accessToken).isNotNull();
    }

    @DisplayName("Refresh Token을 생성한다.")
    @Test
    void createRefreshToken() {
        String refreshToken = jwtProvider.createRefreshToken(1L, "yoon@test.com", "ROLE_MEMBER");

        assertThat(refreshToken).isNotNull();
    }

    @DisplayName("유효한 토큰으로 payload를 조회한다.")
    @Test
    void getPayloadByValidToken() {
        String accessToken = jwtProvider.createAccessToken(1L, "yoon@test.com", "ROLE_MEMBER");

        Claims payload = jwtProvider.getPayload(accessToken);

        assertThat(payload.getSubject()).isEqualTo("1");
        assertThat((String) payload.get("email")).isEqualTo("yoon@test.com");
        assertThat((String) payload.get("role")).isEqualTo("ROLE_MEMBER");
    }

    @DisplayName("유효하지 않은 토큰으로 payload를 조회할 경우 예외가 발생한다.")
    @Test
    void getPayloadInvalidToken() {
        assertThatThrownBy(() -> jwtProvider.getPayload(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("만료된 토큰으로 payload를 조회할 경우 예외가 발생한다.")
    @Test
    void getPayloadByExpiredToken() {
        String expiredToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .setSubject(String.valueOf(1L))
                .setExpiration(new Date((new Date()).getTime() - 1))
                .compact();

        assertThatThrownBy(() -> jwtProvider.getPayload(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);
    }

}