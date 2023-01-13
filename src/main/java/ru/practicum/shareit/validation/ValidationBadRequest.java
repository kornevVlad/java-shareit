package ru.practicum.shareit.validation;

public class ValidationBadRequest extends RuntimeException {

    public ValidationBadRequest(String message) {
        super(message);
    }
}