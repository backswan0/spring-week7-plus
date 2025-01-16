package org.example.expert.common.exception.notfound;

import org.example.expert.common.exception.ErrorCode;

public class ManagerNotFoundException extends NotFoundException {

    public ManagerNotFoundException() {
        super(ErrorCode.NOT_FOUND_MANAGER);
    }
}
