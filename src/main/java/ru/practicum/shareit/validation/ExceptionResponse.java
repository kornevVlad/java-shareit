package ru.practicum.shareit.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionResponse {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "NOT FOUND")
    @ExceptionHandler(ValidationNotFound.class)
    public void errorValidation() {
        log.error("Not fond");
    }
}
