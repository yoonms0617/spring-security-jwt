package com.example.springsecurityjwt.auth.service;

import com.example.springsecurityjwt.auth.domain.LoginMember;
import com.example.springsecurityjwt.common.error.dto.ErrorCode;
import com.example.springsecurityjwt.member.domain.Member;
import com.example.springsecurityjwt.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String ERROR_MESSAGE = ErrorCode.WRONG_EMAIL_PASSWORD.getMessage();

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ERROR_MESSAGE));
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getRole().getKey());
    }

}
