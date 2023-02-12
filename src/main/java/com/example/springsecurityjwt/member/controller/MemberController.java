package com.example.springsecurityjwt.member.controller;

import com.example.springsecurityjwt.auth.domain.LoginMember;
import com.example.springsecurityjwt.member.dto.ProfileResponse;
import com.example.springsecurityjwt.member.dto.SignupRequest;
import com.example.springsecurityjwt.member.service.MemberService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        memberService.signup(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    @Secured("ROLE_MEMBER")
    public ResponseEntity<ProfileResponse> profile(@AuthenticationPrincipal LoginMember loginMember) {
        log.info("LoginMember = {}", loginMember);
        ProfileResponse response = memberService.profile(loginMember.getUsername());
        return ResponseEntity.ok().body(response);
    }

}
