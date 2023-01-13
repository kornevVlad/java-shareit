package ru.practicum.shareit.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.ExceptionState;

@RestControllerAdvice
@Slf4j
public class ExceptionResponse {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "NOT FOUND")
    @ExceptionHandler(ValidationNotFound.class)
    public void errorValidation() {
        log.error("NOT FOUND");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "BAD REQUEST" )
    @ExceptionHandler(ValidationBadRequest.class)
    public void errorValidationBadRequest() {
        log.error("BAD REQUEST");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExceptionState.class)
    public ErrorResponse errorValidationBadRequest(final ExceptionState e) {
        log.error("UNSUPPORTED_STATUS");
        return new ErrorResponse(e.getMessage()) ;
    }
}
