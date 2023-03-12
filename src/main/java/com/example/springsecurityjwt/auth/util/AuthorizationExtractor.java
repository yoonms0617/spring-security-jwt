package com.example.springsecurityjwt.auth.util;

import org.springframework.security.authentication.AuthenticationServiceException;

import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;

public class AuthorizationExtractor {

    private static final String AUTHORIZATION = "Authorization";

    private static final String TOKEN_TYPE = "Bearer";

    public static String extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(TOKEN_TYPE.toLowerCase()))) {
                return value.substring(TOKEN_TYPE.length()).trim();
            }
        }
        throw new AuthenticationServiceException("");
    }

}
