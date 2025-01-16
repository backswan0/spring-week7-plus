package org.example.expert.common.exception.mismatch;

import org.example.expert.common.exception.ErrorCode;

public class UserMismatchException extends MismatchException {

    public UserMismatchException() {
        super(ErrorCode.MISMATCH_USER);
    }
}
