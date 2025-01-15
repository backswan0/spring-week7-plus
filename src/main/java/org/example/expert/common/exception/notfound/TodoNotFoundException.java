package org.example.expert.common.exception.notfound;

import org.example.expert.common.exception.ErrorCode;

public class TodoNotFoundException extends NotFoundException {

    public TodoNotFoundException() {
        super(ErrorCode.NOT_FOUND_TODO);
    }
}