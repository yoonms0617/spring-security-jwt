package com.example.springsecurityjwt.auth.domain;

import com.example.springsecurityjwt.common.domain.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TOKEN")
@NoArgsConstructor
@Getter
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String refreshToken;

    @Column(unique = true)
    private String email;

    public Token(String refreshToken, String email) {
        this.refreshToken = refreshToken;
        this.email = email;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
