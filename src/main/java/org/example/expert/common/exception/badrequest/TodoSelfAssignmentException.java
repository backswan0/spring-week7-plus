package org.example.expert.common.exception.badrequest;

import org.example.expert.common.exception.ErrorCode;

public class TodoSelfAssignmentException extends BadRequestException {

    public TodoSelfAssignmentException() {
        super(ErrorCode.BAD_REQUEST_MANAGER);
    }
}
