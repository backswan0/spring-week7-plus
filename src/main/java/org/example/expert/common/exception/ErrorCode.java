package org.example.expert.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "ERRNF01", "User is not found"),
    NOT_FOUND_TODO(HttpStatus.NOT_FOUND, "ERRNF02", "Todo is not found");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(
        HttpStatus status,
        String code,
        String message
    ) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
