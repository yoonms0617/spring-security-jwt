package com.example.springsecurityjwt.auth.service;

import com.example.springsecurityjwt.auth.domain.Token;
import com.example.springsecurityjwt.auth.dto.TokenRequest;
import com.example.springsecurityjwt.auth.dto.TokenResponse;
import com.example.springsecurityjwt.auth.repository.TokenRepository;

import io.jsonwebtoken.Claims;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    @Transactional
    public void syncRefreshToken(String refreshToken, String email) {
        tokenRepository.findByEmail(email)
                .ifPresentOrElse(
                        token -> token.updateRefreshToken(refreshToken),
                        () -> tokenRepository.save(new Token(refreshToken, email))
                );
    }

    public TokenResponse reissueAccessToken(TokenRequest request) {
        String refreshToken = request.getRefreshToken();
        Claims playload = jwtService.getBodyFromToken(refreshToken);
        String email = playload.getSubject();
        String role = (String) playload.get("role");
        String newAccessToken = jwtService.createAccessToken(email, role);
        return new TokenResponse(newAccessToken);
    }

}
