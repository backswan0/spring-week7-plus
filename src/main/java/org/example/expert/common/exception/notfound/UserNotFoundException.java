package org.example.expert.common.exception.notfound;

import org.example.expert.common.exception.ErrorCode;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}
