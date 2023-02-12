package com.example.springsecurityjwt.common.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE("C000", HttpStatus.BAD_REQUEST.value(), "입력 값이 잘못 되었습니다."),
    WRONG_EMAIL_PASSWORD("C001", HttpStatus.BAD_REQUEST.value(), "이메일 또는 비밀번호를 확인해 주세요."),
    METHOD_NOT_SUPPORTED("C002", HttpStatus.METHOD_NOT_ALLOWED.value(), "지원하지 않는 HTTP 메소드입니다."),

    NOT_FOUND_MEMBER("M000", HttpStatus.NOT_FOUND.value(), "회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL("M001", HttpStatus.CONFLICT.value(), "사용 중인 이메일입니다.");

    private final String code;

    private final int status;

    private final String message;

}
