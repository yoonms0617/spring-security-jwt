package com.example.springsecurityjwt.member.service;

import com.example.springsecurityjwt.common.error.dto.ErrorCode;
import com.example.springsecurityjwt.member.domain.Member;
import com.example.springsecurityjwt.member.dto.SignupRequest;
import com.example.springsecurityjwt.member.exception.DuplicateEmailException;
import com.example.springsecurityjwt.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequest request) {
        validateEmail(request.getEmail());
        String encoded = encryptionPassword(request.getPassword());
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encoded)
                .build();
        memberRepository.save(member);
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private String encryptionPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

}
