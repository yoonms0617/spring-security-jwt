package com.example.springsecurityjwt.common.error;

import com.example.springsecurityjwt.common.error.dto.ErrorResponse;
import com.example.springsecurityjwt.common.error.exception.BaseException;
import com.example.springsecurityjwt.common.error.exception.ErrorType;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e, HttpServletRequest request) {
        String path = getRequestURI(request);
        ErrorResponse response = ErrorResponse.of(e.getErrorType(), path);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request) {
        String path = getRequestURI(request);
        ErrorResponse response = ErrorResponse.of(ErrorType.INVALID_INPUT_VALUE, path);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpServletRequest request) {
        String path = getRequestURI(request);
        ErrorResponse response = ErrorResponse.of(null, path);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        String code = e.getMessage();
        ErrorType errorType = ErrorType.findErrorTypeByCode(code);
        ErrorResponse response = ErrorResponse.of(errorType, path);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    private String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

}
