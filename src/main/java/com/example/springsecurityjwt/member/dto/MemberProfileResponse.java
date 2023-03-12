package com.example.springsecurityjwt.member.dto;

import lombok.Getter;

@Getter
public class MemberProfileResponse {

    private final String name;

    private final String email;

    public MemberProfileResponse(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
