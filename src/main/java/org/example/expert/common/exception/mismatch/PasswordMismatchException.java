package org.example.expert.common.exception.mismatch;

import org.example.expert.common.exception.ErrorCode;

public class PasswordMismatchException extends MismatchException {

    public PasswordMismatchException() {
        super(ErrorCode.MISMATCH_PASSWORD);
    }
}
