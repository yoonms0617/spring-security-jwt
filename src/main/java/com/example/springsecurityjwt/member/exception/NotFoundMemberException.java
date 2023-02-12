package com.example.springsecurityjwt.member.exception;

import com.example.springsecurityjwt.common.error.dto.ErrorCode;
import com.example.springsecurityjwt.common.error.exception.BaseException;

public class NotFoundMemberException extends BaseException {

    public NotFoundMemberException(ErrorCode errorCode) {
        super(errorCode);
    }

}
