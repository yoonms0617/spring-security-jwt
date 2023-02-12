package com.example.springsecurityjwt.auth.filter;

import com.example.springsecurityjwt.auth.domain.LoginMember;
import com.example.springsecurityjwt.auth.support.JwtProvider;

import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String[] NO_FILTER_REQUEST = {
            "/member/signup", "/member/login"
    };

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtFilter RUN!!!");
        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.substring(7);
        log.info("AccessToken = {}", accessToken);
        Claims payload = jwtProvider.getPayload(accessToken);
        Authentication authentication = createAuthentication(payload);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private Authentication createAuthentication(Claims payload) {
        String role = (String) payload.get("role");
        UserDetails principal = new LoginMember(
                Long.valueOf(payload.getSubject()),
                (String) payload.get("email"),
                "",
                role
        );
        return UsernamePasswordAuthenticationToken.authenticated(principal, "", AuthorityUtils.createAuthorityList(role));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        for (String uri : NO_FILTER_REQUEST) {
            if (request.getRequestURI().equals(uri)) {
                return true;
            }
        }
        return false;
    }

}
