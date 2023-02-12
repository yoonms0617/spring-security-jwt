package com.example.springsecurityjwt.member.controller;

import com.example.springsecurityjwt.auth.dto.LoginRequest;
import com.example.springsecurityjwt.common.error.dto.ErrorCode;
import com.example.springsecurityjwt.member.dto.SignupRequest;
import com.example.springsecurityjwt.member.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MemberController TEST")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    private final SignupRequest signupRequest =
            new SignupRequest("Yoon", "yoon@test.com", "12345678");

    private final LoginRequest loginRequest =
            new LoginRequest("yoon@test.com", "12345678");

    private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원가입 요청을 한다.")
    @Test
    void signup() throws Exception {
        mockMvc
                .perform(post("/member/signup")
                        .content(objectMapper.writeValueAsString(signupRequest))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("회원가입 요청시 이메일이 중복되면 예외가 발생한다.")
    @Test
    void duplicateEmailSignup() throws Exception {
        ErrorCode errorCode = ErrorCode.DUPLICATE_EMAIL;

        memberService.signup(signupRequest);

        mockMvc
                .perform(post("/member/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.status").value(errorCode.getStatus()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                .andDo(print());
    }

    @DisplayName("회원가입 요청시 유효하지 않은 값을 보내면 예외가 발생한다.")
    @Test
    void invalidInputValueSignup() throws Exception {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;

        SignupRequest invalidRequest = new SignupRequest("", "", "");

        mockMvc
                .perform(post("/member/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.status").value(errorCode.getStatus()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                .andDo(print());
    }

    @DisplayName("로그인 요청을 한다.")
    @Test
    void login() throws Exception {
        memberService.signup(signupRequest);

        mockMvc
                .perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("잘못된 이메일 또는 비밀번호로 로그인 요청을 하면 예외가 발생한다.")
    @Test
    void loginWrongEmailPassword() throws Exception {
        ErrorCode errorCode = ErrorCode.WRONG_EMAIL_PASSWORD;

        mockMvc
                .perform(post("/member/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.status").value(errorCode.getStatus()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                .andDo(print());
    }

}