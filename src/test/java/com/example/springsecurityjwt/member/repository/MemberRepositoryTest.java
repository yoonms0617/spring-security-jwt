package com.example.springsecurityjwt.member.repository;

import com.example.springsecurityjwt.member.domain.Member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class MemberRepositoryTest {

    private static final String NOT_EXIST_EMAIL = "존재하지 않는 이메일";

    private Member member;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .name("윤민수")
                .email("yoon@test.com")
                .password("1234")
                .build();
    }

    @DisplayName("DB에 Member를 저장한다.")
    @Test
    void save() {
        Member save = memberRepository.save(member);

        assertThat(save.getId()).isNotNull();
        assertThat(save.getName()).isEqualTo(member.getName());
        assertThat(save.getEmail()).isEqualTo(member.getEmail());
        assertThat(save.getPassword()).isEqualTo(member.getPassword());
    }

    @DisplayName("DB에 이미 존재하는 Email을 가진 Member를 저장하면 예외가 발생한다.")
    @Test
    void saveDuplicateEmail() {
        memberRepository.save(member);
        Member duplicateEmailMember = Member.builder().email(member.getEmail()).build();

        assertThatThrownBy(() -> memberRepository.save(duplicateEmailMember))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("식별자로 Member를 조회한다.")
    @Test
    void findById() {
        Member save = memberRepository.save(member);

        Member findMember = memberRepository.findById(save.getId())
                .orElseThrow(NoSuchElementException::new);

        assertThat(findMember.getId()).isNotNull();
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
    }

    @DisplayName("Email의 존재 여부를 확인한다.")
    @Test
    void existsByEmail() {
        memberRepository.save(member);

        boolean existEmail = memberRepository.existsByEmail(member.getEmail());
        boolean notExistEmail = memberRepository.existsByEmail(NOT_EXIST_EMAIL);

        assertThat(existEmail).isTrue();
        assertThat(notExistEmail).isFalse();
    }

}