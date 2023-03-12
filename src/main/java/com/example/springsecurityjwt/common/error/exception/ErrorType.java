package com.example.springsecurityjwt.common.error.exception;

import lombok.Getter;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum ErrorType {

    ERROR_TYPE_NOT_SUPPORTED("ERR000", HttpStatus.BAD_REQUEST.value(), "지원하지 않는 예외 유형입니다."),
    INVALID_INPUT_VALUE("ERR001", HttpStatus.BAD_REQUEST.value(), "입력 값이 잘못되었습니다."),
    METHOD_NOT_SUPPORTED("ERR002", HttpStatus.METHOD_NOT_ALLOWED.value(), "요청 메소드가 잘못되었습니다."),
    DUPLICATE_EMAIL("ERR003", HttpStatus.CONFLICT.value(), "사용 중인 이메일입니다."),
    NOT_FOUND_MEMBER("ERR004", HttpStatus.BAD_REQUEST.value(), "사용자를 찾을 수 없습니다."),
    WRONG_EMAIL_PASSWORD("ERR005", HttpStatus.BAD_REQUEST.value(), "이메일 또는 비밀번호를 확인해 주세요"),
    EXPIERD_TOKEN("ERR006", HttpStatus.UNAUTHORIZED.value(), "토큰이 만료되었습니다."),
    MAL_FORMED_TOKEN("ERR007", HttpStatus.UNAUTHORIZED.value(), "유효하지 않는 토큰입니다."),
    INVALID_SCRETKEY("ERR008", HttpStatus.UNAUTHORIZED.value(), "토큰의 시크릿 키가 변조되었습니다.");

    private final String code;

    private final int status;

    private final String mesage;

    ErrorType(String code, int status, String mesage) {
        this.code = code;
        this.status = status;
        this.mesage = mesage;
    }

    public static ErrorType findErrorTypeByCode(String code) {
        return Arrays.stream(ErrorType.values())
                .filter(errorType -> errorType.getCode().equals(code))
                .findFirst()
                .orElse(ErrorType.ERROR_TYPE_NOT_SUPPORTED);
    }

}
