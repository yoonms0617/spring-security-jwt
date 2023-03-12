package com.example.springsecurityjwt.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenInvalidFormException extends AuthenticationException {

    public TokenInvalidFormException(String msg) {
        super(msg);
    }

}
