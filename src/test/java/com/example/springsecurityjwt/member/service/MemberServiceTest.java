package com.example.springsecurityjwt.member.service;


import com.example.springsecurityjwt.member.domain.Member;
import com.example.springsecurityjwt.member.dto.SignupRequest;
import com.example.springsecurityjwt.member.exception.DuplicateEmailException;
import com.example.springsecurityjwt.member.repository.MemberRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final String PASSWORD_ENCRYPTION_ALGORITHM = "{bcrypt}";

    private final SignupRequest request = new SignupRequest("Yoon", "yoon@test.com", "12345678");

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("회원가입을 한다.")
    @Test
    void signup() {
        Member member = createMemberEntity(request);

        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(passwordEncoder.encode(any())).willReturn(member.getPassword());
        given(memberRepository.save(any())).willReturn(member);

        memberService.signup(request);

        assertThat(member.getName()).isEqualTo(request.getName());
        assertThat(member.getEmail()).isEqualTo(request.getEamil());
        assertThat(member.getPassword()).startsWith(PASSWORD_ENCRYPTION_ALGORITHM);
        then(memberRepository).should().existsByEmail(any());
        then(passwordEncoder).should().encode(any());
        then(memberRepository).should().save(any());
    }

    @DisplayName("회원가입시 이메일이 중복되면 예외가 발생한다.")
    @Test
    void duplicateEmailSignup() {
        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.existsByEmail(any())).willThrow(DuplicateEmailException.class);

        assertThatThrownBy(() -> memberService.signup(request))
                .isInstanceOf(DuplicateEmailException.class);

        then(memberRepository).should().existsByEmail(any());
        then(passwordEncoder).should(never()).encode(any());
        then(memberRepository).should(never()).save(any());
    }

    private Member createMemberEntity(SignupRequest request) {
        String encoded = encryptionPassword(request.getPassword());
        return Member.builder()
                .name(request.getName())
                .email(request.getEamil())
                .password(encoded)
                .build();
    }

    private String encryptionPassword(String rawPassword) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder.encode(rawPassword);
    }

}