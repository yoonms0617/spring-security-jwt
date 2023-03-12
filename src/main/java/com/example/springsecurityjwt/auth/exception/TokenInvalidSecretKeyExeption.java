package com.example.springsecurityjwt.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenInvalidSecretKeyExeption extends AuthenticationException {

    public TokenInvalidSecretKeyExeption(String msg) {
        super(msg);
    }

}
