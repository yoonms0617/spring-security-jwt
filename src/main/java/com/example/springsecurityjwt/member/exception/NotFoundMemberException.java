package com.example.springsecurityjwt.member.exception;

import com.example.springsecurityjwt.common.error.exception.BaseException;
import com.example.springsecurityjwt.common.error.exception.ErrorType;

public class NotFoundMemberException extends BaseException {

    public NotFoundMemberException(ErrorType errorType) {
        super(errorType);
    }

}
