package org.example.expert.common.exception.mismatch;

import lombok.Getter;
import org.example.expert.common.exception.ErrorCode;

@Getter
public class MismatchException extends RuntimeException {

    private final ErrorCode errorCode;

    public MismatchException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
