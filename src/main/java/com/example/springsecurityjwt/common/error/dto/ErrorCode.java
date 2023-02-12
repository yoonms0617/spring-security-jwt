package com.example.springsecurityjwt.common.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE("C000", HttpStatus.BAD_REQUEST.value(), "입력 값이 잘못 되었습니다."),

    DUPLICATE_EMAIL("M000", HttpStatus.CONFLICT.value(), "사용 중인 이메일입니다.");

    private final String code;

    private final int status;

    private final String message;

}
