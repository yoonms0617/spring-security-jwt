package com.example.springsecurityjwt.member.exception;

import com.example.springsecurityjwt.common.error.dto.ErrorCode;
import com.example.springsecurityjwt.common.error.exception.BaseException;

public class DuplicateEmailException extends BaseException {

    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode);
    }

}
