package com.example.springsecurityjwt.auth.domain;

import lombok.Getter;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
public class LoginMember extends User {

    private final Long id;

    private final String role;

    public LoginMember(Long id, String username, String password, String role) {
        super(username, password, AuthorityUtils.createAuthorityList(role));
        this.id = id;
        this.role = role;
    }

}
