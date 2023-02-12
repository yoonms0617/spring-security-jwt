package com.example.springsecurityjwt.member.controller;

import com.example.springsecurityjwt.auth.config.SecurityConfig;
import com.example.springsecurityjwt.common.error.dto.ErrorCode;
import com.example.springsecurityjwt.member.dto.SignupRequest;
import com.example.springsecurityjwt.member.exception.DuplicateEmailException;
import com.example.springsecurityjwt.member.service.MemberService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MemberController TEST")
@WebMvcTest
@Import(SecurityConfig.class)
class MemberControllerTest {

    private final SignupRequest request = new SignupRequest("Yoon", "yoon@test.com", "12345678");

    private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @DisplayName("회원가입 요청을 한다.")
    @Test
    void signup() throws Exception {
        willDoNothing().given(memberService).signup(any());

        mockMvc
                .perform(post("/member/signup")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @DisplayName("회원가입 요청시 이메일이 중복되면 예외가 발생한다.")
    @Test
    void duplicateEmailSignup() throws Exception {
        ErrorCode errorCode = ErrorCode.DUPLICATE_EMAIL;
        willThrow(new DuplicateEmailException(errorCode)).given(memberService).signup(any());

        mockMvc
                .perform(post("/member/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(jsonPath("$.status").value(errorCode.getStatus()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                .andDo(print())
                .andReturn();
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
                .andDo(print())
                .andReturn();
    }

}