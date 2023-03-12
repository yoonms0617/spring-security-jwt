package com.example.springsecurityjwt.common.error.dto;

import com.example.springsecurityjwt.common.error.exception.ErrorType;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final LocalDateTime timeStamp;

    private final String code;

    private final int status;

    private final String message;

    private final String path;

    private ErrorResponse(ErrorType errorType, String path) {
        this.timeStamp = LocalDateTime.now();
        this.code = errorType.getCode();
        this.status = errorType.getStatus();
        this.message = errorType.getMesage();
        this.path = path;
    }

    public static ErrorResponse of(ErrorType errorType, String path) {
        return new ErrorResponse(errorType, path);
    }

}
