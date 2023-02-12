package com.example.springsecurityjwt.common.error.exception;

import com.example.springsecurityjwt.common.error.dto.ErrorCode;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
