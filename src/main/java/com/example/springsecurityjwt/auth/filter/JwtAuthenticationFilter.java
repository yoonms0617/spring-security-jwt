package com.example.springsecurityjwt.auth.filter;

import com.example.springsecurityjwt.auth.service.JwtService;
import com.example.springsecurityjwt.auth.util.AuthorizationExtractor;

import io.jsonwebtoken.Claims;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] NOT_FILTER_REQUEST = {
            "/api/member/login",
            "/api/member/signup",
            "/api/auth/token/refresh"
    };

    private final JwtService jwtService;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationFilter RUN");
        try {
            String token = AuthorizationExtractor.extract(request);
            jwtService.validateToken(token);
            Claims body = jwtService.getBodyFromToken(token);
            String email = body.getSubject();
            String role = (String) body.get("role");
            Authentication authentication = createAuthentication(email, role);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            authenticationEntryPoint.commence(request, response, e);
        }
    }

    private Authentication createAuthentication(String email, String role) {
        return UsernamePasswordAuthenticationToken
                .authenticated(email, "", AuthorityUtils.createAuthorityList(role));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        for (String uri : NOT_FILTER_REQUEST) {
            if (request.getRequestURI().equals(uri)) {
                return true;
            }
        }
        return false;
    }

}
