package com.example.springsecurityjwt.auth.domain;

import lombok.Getter;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
public class AuthMember extends User {

    private final Long id;

    private final String email;

    private final String role;

    public AuthMember(Long id, String email, String password, String role) {
        super(email, password, AuthorityUtils.createAuthorityList(role));
        this.id = id;
        this.email = email;
        this.role = role;
    }

}
