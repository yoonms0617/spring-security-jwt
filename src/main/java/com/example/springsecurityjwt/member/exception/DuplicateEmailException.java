package com.example.springsecurityjwt.member.exception;

import com.example.springsecurityjwt.common.error.exception.BaseException;
import com.example.springsecurityjwt.common.error.exception.ErrorType;

public class DuplicateEmailException extends BaseException {

    public DuplicateEmailException(ErrorType errorType) {
        super(errorType);
    }

}
