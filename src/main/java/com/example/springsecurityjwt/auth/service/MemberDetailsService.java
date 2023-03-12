package com.example.springsecurityjwt.auth.service;

import com.example.springsecurityjwt.auth.domain.AuthMember;
import com.example.springsecurityjwt.common.error.exception.ErrorType;
import com.example.springsecurityjwt.member.domain.Member;
import com.example.springsecurityjwt.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorType.NOT_FOUND_MEMBER.getCode()));
        return new AuthMember(member.getId(), member.getEmail(), member.getPassword(), member.getRole().getKey());
    }

}
