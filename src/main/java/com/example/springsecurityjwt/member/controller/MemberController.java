package com.example.springsecurityjwt.member.controller;

import com.example.springsecurityjwt.auth.annotation.LoginMember;
import com.example.springsecurityjwt.auth.annotation.RoleMember;
import com.example.springsecurityjwt.member.dto.MemberProfileResponse;
import com.example.springsecurityjwt.member.dto.MemberProfileUpdateRequest;
import com.example.springsecurityjwt.member.dto.MemberSignupRequest;
import com.example.springsecurityjwt.member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.net.URI;

@RestController
@RequestMapping("/api/member")
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberSignupRequest request) {
        memberService.signup(request);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/profile")
    @RoleMember
    public ResponseEntity<MemberProfileResponse> profile(@LoginMember String email) {
        MemberProfileResponse response = memberService.profile(email);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/profile/update")
    @RoleMember
    public ResponseEntity<MemberProfileResponse> updateProfile(@LoginMember String email,
                                                               @RequestBody MemberProfileUpdateRequest request) {
        MemberProfileResponse response = memberService.updateProfile(email, request);
        return ResponseEntity.ok().body(response);
    }

}
