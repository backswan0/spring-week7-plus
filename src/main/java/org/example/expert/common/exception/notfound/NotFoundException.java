package org.example.expert.common.exception.notfound;

import lombok.Getter;
import org.example.expert.common.exception.ErrorCode;

@Getter
public class NotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
