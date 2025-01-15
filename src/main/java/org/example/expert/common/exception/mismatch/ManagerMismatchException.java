package org.example.expert.common.exception.mismatch;

import org.example.expert.common.exception.ErrorCode;

public class ManagerMismatchException extends MismatchException {

    public ManagerMismatchException() {
        super(ErrorCode.MISMATCH_MANAGER);
    }
}
