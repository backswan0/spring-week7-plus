package org.example.expert.common.exception;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.common.exception.badrequest.BadRequestException;
import org.example.expert.common.exception.mismatch.MismatchException;
import org.example.expert.common.exception.notfound.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(
        NotFoundException ex
    ) {
        return handleException(
            ex.getErrorCode().getCode(),
            ex.getErrorCode().getMessage(),
            ex.getErrorCode().getStatus()
        );
    }

    @ExceptionHandler(MismatchException.class)
    public ResponseEntity<Map<String, String>> handleMismatchException(
        MismatchException ex
    ) {
        return handleException(
            ex.getErrorCode().getCode(),
            ex.getErrorCode().getMessage(),
            ex.getErrorCode().getStatus()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(
        BadRequestException ex
    ) {
        return handleException(
            ex.getErrorCode().getCode(),
            ex.getErrorCode().getMessage(),
            ex.getErrorCode().getStatus()
        );
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String, Object>> invalidRequestExceptionException(
        InvalidRequestException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return getErrorResponse(status, ex.getMessage());
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Map<String, Object>> handleServerException(ServerException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return getErrorResponse(status, ex.getMessage());
    }

    public ResponseEntity<Map<String, Object>> getErrorResponse(
        HttpStatus status,
        String message
    ) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.name());
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);
    }

    private ResponseEntity<Map<String, String>> handleException(
        String errorCode,
        String errorMessage,
        HttpStatus status
    ) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("errorCode", errorCode);
        response.put("errorMessage", errorMessage);

        log.info("에러 발생 >>> 코드: {}", errorCode);
        log.info("에러 발생 >>> 메시지: {}", errorMessage);

        return new ResponseEntity<>(response, status);
    }
}

