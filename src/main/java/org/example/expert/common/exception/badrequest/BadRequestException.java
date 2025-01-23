package org.example.expert.common.exception.badrequest;

import lombok.Getter;
import org.example.expert.common.exception.ErrorCode;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}