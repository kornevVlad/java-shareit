package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorResponse {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Error errorValidationBadRequest(final IllegalArgumentException e) {
        log.error("UNSUPPORTED_STATUS");
        return new Error(e.getMessage());
    }
}
