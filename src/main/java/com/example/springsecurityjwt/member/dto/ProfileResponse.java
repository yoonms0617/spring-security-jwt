package com.example.springsecurityjwt.member.dto;

import com.example.springsecurityjwt.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {

    private final String name;

    private final String email;

    public ProfileResponse(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }

}
