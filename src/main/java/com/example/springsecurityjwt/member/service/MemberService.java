package com.example.springsecurityjwt.member.service;

import com.example.springsecurityjwt.common.error.exception.ErrorType;
import com.example.springsecurityjwt.member.domain.Member;
import com.example.springsecurityjwt.member.dto.MemberProfileResponse;
import com.example.springsecurityjwt.member.dto.MemberProfileUpdateRequest;
import com.example.springsecurityjwt.member.dto.MemberSignupRequest;
import com.example.springsecurityjwt.member.exception.DuplicateEmailException;
import com.example.springsecurityjwt.member.exception.NotFoundMemberException;
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
    public void signup(MemberSignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(ErrorType.DUPLICATE_EMAIL);
        }
        String encoded = passwordEncoder.encode(request.getPassword());
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encoded)
                .build();
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberProfileResponse profile(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberException(ErrorType.NOT_FOUND_MEMBER));
        return new MemberProfileResponse(member.getName(), member.getEmail());
    }

    @Transactional
    public MemberProfileResponse updateProfile(String email, MemberProfileUpdateRequest request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundMemberException(ErrorType.NOT_FOUND_MEMBER));
        member.updateName(request.getName());
        return new MemberProfileResponse(member.getName(), member.getEmail());
    }

}
